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

package uk.co.caprica.picam.app;

import uk.co.caprica.picam.Camera;
import uk.co.caprica.picam.CameraConfiguration;
import uk.co.caprica.picam.CameraException;
import uk.co.caprica.picam.CaptureFailedException;
import uk.co.caprica.picam.FilePictureCaptureHandler;
import uk.co.caprica.picam.NativeLibraryException;
import uk.co.caprica.picam.enums.AutomaticWhiteBalanceMode;
import uk.co.caprica.picam.enums.DynamicRangeCompressionStrength;
import uk.co.caprica.picam.enums.Encoding;
import uk.co.caprica.picam.enums.ExposureMeteringMode;
import uk.co.caprica.picam.enums.ExposureMode;

import java.nio.file.Path;

import static uk.co.caprica.picam.CameraConfiguration.cameraConfiguration;
import static uk.co.caprica.picam.PicamNativeLibrary.installTempLibrary;
import static uk.co.caprica.picam.PicamNativeLibrary.loadLibrary;
import static uk.co.caprica.picam.app.Environment.dumpEnvironment;

/**
 * A very basic application demonstrating how to setup the native library, create a camera and take a picture.
 */
public final class Snap {

    public static void main(String[] args) {
        dumpEnvironment();

        try {
            // First check if the system property override was specified, and load the native library from there
            Path path = loadLibrary();
            if (path == null) {
                // No system property was set, extract the bundled native library from the jar
                System.out.printf("Temporarily installed picam native library to %s%n%n", installTempLibrary());
            } else {
                System.out.printf("Loaded picam native library at %s%n%n", path);
            }
        }
        catch (NativeLibraryException e) {
            System.err.printf("Failed to extract, install or load the picam native library: %s%n", e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }

        if (args.length != 3) {
            System.err.println("Usage: <width> <height> <filename>.jpg");
            System.exit(1);
        }

        int width = Integer.parseInt(args[0]);
        int height = Integer.parseInt(args[1]);
        String filename = args[2];

        CameraConfiguration config = cameraConfiguration()
            .width(width)
            .height(height)
            .encoding(Encoding.JPEG)
            .quality(85)
            .brightness(50)
            .exposureMode(ExposureMode.AUTO)
            .exposureMeteringMode(ExposureMeteringMode.AVERAGE)
            .automaticWhiteBalanceMode(AutomaticWhiteBalanceMode.AUTO)
            .colourEffect(false, 128, 128)
            .dynamicRangeCompressionStrength(DynamicRangeCompressionStrength.OFF)
            .captureTimeout(10000);

        FilePictureCaptureHandler pictureCaptureHandler = new FilePictureCaptureHandler(filename);

        try (Camera camera = new Camera(config)) {
            try {
                System.out.println("Taking picture...");
                camera.takePicture(pictureCaptureHandler, 5000);
                System.out.println("...success!");
            }
            catch (CaptureFailedException e) {
                System.err.printf("Failed to take picture: %s%n", e.getMessage());
                e.printStackTrace();
            }
        }
        catch (CameraException e) {
            System.err.printf("Failed to create camera", e.getMessage());
            e.printStackTrace();;
        }
    }

}
