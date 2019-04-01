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
 * <p>
 * Creating a camera instance will automatically "open" it so that it is ready for taking pictures.
 * <p>
 * If an error occurs, or if you no longer need the camera instance, you should {@link #close()} it.
 * <p>
 * Using try-with-resources is recommended to make sure the camera instance is closed, for example:
 * <pre>
 * try (Camera camera = new Camera(config) {
 *     camera.takePicture(new FilePictureCaptureHandler("capture.jpg"));
 * }
 * </pre>
 * <p>
 * A closed camera can be reopened via {@link #open()} if you are attempting to recover from an error, otherwise simply
 * create a new camera instance.
 * <p>
 * While the camera is in use you must make sure to keep references to the camera and any {@link PictureCaptureHandler}
 * objects that you are using to prevent them from being garbage-collected. If you do not, a fatal crash may occur and
 * kill your application.
 * <p>
 * The underlying camera is a hardware resource and as such is fundamentally <strong>single-threaded</strong>. You must
 * <strong>not</strong> access the camera from multiple threads concurrently.
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
     * Create a camera component with reasonable default configuration.
     * <p>
     * The camera will automatically be opened.
     * <p>
     * The camera should be closed via {@link #close()} when it is no longer needed.
     *
     * @see #Camera(CameraConfiguration)
     * @see #close()
     */
    public Camera() {
        this(null);
    }

    /**
     * Create a camera component.
     * <p>
     * The camera will automatically be opened.
     * <p>
     * The camera should be closed via {@link #close()} when it is no longer needed.
     *
     * @see #close()
     *
     * @param cameraConfiguration camera configuration
     */
    public Camera(CameraConfiguration cameraConfiguration) {
        this.cameraConfiguration = cameraConfiguration;
        open();
    }

    /**
     * Open the camera, creating all of the necessary native resources.
     * <p>
     * If the camera is already open this method will do nothing.
     *
     * @see #close()
     * @return <code>true</code> if the camera is open; <code>false</code> if it is not
     */
    public boolean open() {
        if (!opened) {
            opened = create(cameraConfiguration);
        }
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
     * @param pictureCaptureHandler handler that will receive the picture capture data
     * @param <T> type that will be returned by the picture capture handler
     * @return picture capture handler result
     * @throws CaptureFailedException if an error occurs
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
     * @param pictureCaptureHandler handler that will receive the picture capture data
     * @param delay delay before taking the picture, specified in milliseconds
     * @param <T> type that will be returned by the picture capture handler
     * @return picture capture handler result
     * @throws CaptureFailedException if an error occurs
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
     * The camera can be reopened via {@link #open()}.
     * <p>
     * If the camera is already closed this method will do nothing.
     *
     * @see #open()
     */
    @Override
    public void close() {
        if (opened) {
            destroy();
            opened = false;
        }
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
