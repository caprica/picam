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

import uk.co.caprica.picam.enums.AutomaticWhiteBalanceMode;
import uk.co.caprica.picam.enums.Encoding;
import uk.co.caprica.picam.enums.ExposureMeteringMode;
import uk.co.caprica.picam.enums.ExposureMode;

import java.util.Date;

import static uk.co.caprica.picam.CameraConfiguration.cameraConfiguration;

/**
 * A simple test that repeatedly captures a picture (to a byte array, then throws it away), to help check for races and
 * other similar issues.
 */
public class StressTest {

    public static void main(String[] args) throws Exception {
        System.out.println("Installed native library to " + PicamNativeLibrary.installTempLibrary());

        Camera camera = new Camera(
            cameraConfiguration()
                .width(1920)
                .height(1080)
                .automaticWhiteBalance(AutomaticWhiteBalanceMode.AUTO)
                .exposureMode(ExposureMode.AUTO)
                .exposureMeteringMode(ExposureMeteringMode.MATRIX)
                .encoding(Encoding.JPEG)
                .quality(85)
                .captureTimeout(10000)
        );

        int count = 0;

        System.out.printf("STARTED AT %s%n", new Date());

        long started = System.currentTimeMillis();

        while (true) {
            try {
                PictureCaptureHandler<?> captureHandler = new ByteArrayPictureCaptureHandler(40000);
                camera.takePicture(captureHandler);
                count++;
                if (count % 50 == 0) {
                    long now = System.currentTimeMillis();

                    int seconds = (int)((now - started) / 1000);
                    float rate = (float) count / (float) seconds;

                    System.err.printf("%-14s: total %6d, %01.1f/s%n", formatTime(now-started), count, rate);
                }
            }
            catch (CaptureFailedException e) {
                camera.close();
                System.out.println("Capture failed: " + e.getMessage());
                boolean reopen = camera.open();
                if (!reopen) {
                    System.out.println("Failed to reopen camera");
                    break;
                }
                System.out.println("Camera successfully reopened");
            }
            catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }

        // Join here so we could e.g. attach a profiler after the failure
//        Thread.currentThread().join();
    }

    private static String formatTime(long value) {
        value /= 1000;
        int hours = (int) value / 3600;
        int remainder = (int) value - hours * 3600;
        int minutes = remainder / 60;
        remainder = remainder - minutes * 60;
        int seconds = remainder;
        return String.format("%d:%02d:%02d", hours, minutes, seconds);
    }

}
