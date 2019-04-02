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

package uk.co.caprica.picam.tutorial.failures;

import uk.co.caprica.picam.Camera;
import uk.co.caprica.picam.CameraConfiguration;
import uk.co.caprica.picam.CaptureFailedException;
import uk.co.caprica.picam.NativeLibraryException;
import uk.co.caprica.picam.PictureCaptureHandler;
import uk.co.caprica.picam.enums.AutomaticWhiteBalanceMode;
import uk.co.caprica.picam.enums.Encoding;

import static uk.co.caprica.picam.CameraConfiguration.cameraConfiguration;
import static uk.co.caprica.picam.PicamNativeLibrary.installTempLibrary;

public class MyCameraApplication9 {

    public static void main(String[] args) throws NativeLibraryException {
        installTempLibrary();

        CameraConfiguration config = cameraConfiguration()
            .width(1920)
            .height(1080)
            .automaticWhiteBalance(AutomaticWhiteBalanceMode.AUTO)
            .encoding(Encoding.JPEG)
            .quality(85);

        Camera camera = new Camera(config);

        PictureCaptureHandler<?> pictureCaptureHandler = new MyPictureCaptureHandler();

        for (;;) {
            try {
                Thread.sleep(5000);
                camera.takePicture(pictureCaptureHandler);
            }
            catch (InterruptedException e) {
                System.out.println("Thread interrupted while sleeping: " + e.getMessage());
                System.exit(-1);
            }
            catch (CaptureFailedException e) {
                System.out.println("Capture failed, reopening camera: " + e.getMessage());
                camera.close();
                if (!camera.open()) {
                    System.out.println("Failed to reopen the camera");
                    System.exit(-1);
                }
            }
        }
    }

    private static class MyPictureCaptureHandler implements PictureCaptureHandler<Void> {

        @Override
        public void begin() throws Exception {
        }

        @Override
        public int pictureData(byte[] data) throws Exception {
            return 0;
        }

        @Override
        public void end() throws Exception {
        }

        @Override
        public Void result() {
            return null;
        }

    }

}