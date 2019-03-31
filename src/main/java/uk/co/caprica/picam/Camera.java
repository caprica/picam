/*
 * This file is part of picam.
 *
 * picam is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * picam is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with picam.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2016-2019 Caprica Software Limited.
 */

package uk.co.caprica.picam;

/**
 * Camera.
 */
public final class Camera implements AutoCloseable {

    /**
     * Camera configuration, may be <code>null</code>.
     */
    private CameraConfiguration cameraConfiguration;

    /**
     * Flag tracks whether this component is currently "open" or not.
     *
     * @see #open()
     * @see #close()
     */
    private boolean opened;

    /**
     * Create a camera component with default configuration.
     * <p>
     * A camera must be opened before taking pictures.
     * <p>
     * A camera should be closed when it is no longer needed.
     *
     * @see #Camera(CameraConfiguration)
     * @see #open()
     * @see #close()
     */
    public Camera() {
    }

    /**
     * Create a camera component.
     * <p>
     * A camera must be opened before taking pictures.
     * <p>
     * A camera should be closed when it is no longer needed.
     *
     * @see #open()
     * @see #close()
     *
     * @param cameraConfiguration camera configuration
     */
    public Camera(CameraConfiguration cameraConfiguration) {
        this.cameraConfiguration = cameraConfiguration;
    }

    /**
     * Open the camera, creating all of the necessary native resources.
     *
     * @see #close()
     */
    public boolean open() {
        opened = create(cameraConfiguration);
        return opened;
    }

    /**
     * Take a picture.
     * <p>
     * The camera must be open before taking a picture.
     * <p>
     * The capture handler will be invoked on a <strong>native</strong> callback thread.
     * <p>
     * The calling application must make sure that the {@link PictureCaptureHandler} instance is kept in-scope and
     * prevented from being garbage collected.
     *
     * @see #open()
     * @see #takePicture(PictureCaptureHandler, int)
     *
     * @param pictureCaptureHandler
     * @param <T>
     * @return
     * @throws CaptureFailedException
     */
    public <T> T takePicture(PictureCaptureHandler<T> pictureCaptureHandler) throws CaptureFailedException {
        return takePicture(pictureCaptureHandler ,0);
    }

    /**
     * Take a picture, with an initial capture delay.
     * <p>
     * The camera must be open before taking a picture.
     * <p>
     * The capture handler will be invoked on a <strong>native</strong> callback thread.
     * <p>
     * The calling application must make sure that the {@link PictureCaptureHandler} instance is kept in-scope and
     * prevented from being garbage collected.
     *
     * @see #open()
     *
     * @param pictureCaptureHandler
     * @param delay
     * @param <T>
     * @return
     * @throws CaptureFailedException
     */
    public <T> T takePicture(PictureCaptureHandler<T> pictureCaptureHandler, int delay) throws CaptureFailedException {
        if (!opened) {
            throw new IllegalStateException("The camera must be opened first");
        }

        if (capture(pictureCaptureHandler, delay)) {
            return pictureCaptureHandler.result();
        } else {
            throw new CaptureFailedException("Failed to trigger the capture");
        }
    }

    /**
     * Close the camera, freeing up all of the associated native resources.
     * <p>
     * The camera can be reopened.
     *
     * @see #open()
     */
    @Override
    public void close() {
        destroy();
        opened = false;
    }

    /**
     * Private native method used to create the native camera component and all associated native resources.
     *
     * @param cameraConfiguration
     * @return
     */
    private native boolean create(CameraConfiguration cameraConfiguration);

    /**
     * Private native method used to trigger a capture.
     *
     * @param handler
     * @param delay
     * @throws CaptureFailedException
     */
    private native boolean capture(PictureCaptureHandler<?> handler, int delay) throws CaptureFailedException;

    /**
     * Private native method used to destroy the native camera component and all associated native resources.
     */
    private native void destroy();

}
