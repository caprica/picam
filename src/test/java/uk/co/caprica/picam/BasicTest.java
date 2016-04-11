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
 * Copyright 2016 Caprica Software Limited.
 */

package uk.co.caprica.picam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.picam.enums.ImageEffect;

import java.io.File;

import static uk.co.caprica.picam.CameraConfiguration.cameraConfiguration;
import static uk.co.caprica.picam.bindings.LibBcm.bcm;

// FIXME only problem seems to be error/warning messages on destroy
//       not sure if this is expected or not, it does NOT appear with Raspistill, but I think maybe it routes logging somewhere
//       check clean-up is robust and not duplicated (do I need explicit disconnect ports?)
//       maybe some race? i've seen port_enabled = true twice in a row

// FIXME check all JNA reads/writes are actually necessary

// FIXME occasionally it does appear to hang during capture, it seems the native library never sends the last frame in these cases

// FIXME extend ParameterStructure - why is the type not set? e.g. for int32, rational? does it matter?

public class BasicTest {

    private final Logger logger = LoggerFactory.getLogger(BasicTest.class);

    public static void main(String[] args) throws Exception {
        new BasicTest(args);
    }

    private BasicTest(String[] args) throws Exception {
        logger.info("Test()");

        bcm.bcm_host_init(); // FIXME seems not needed?

        int width = Integer.parseInt(args[0]);
        int height = Integer.parseInt(args[1]);

        String filename = args[2];

        CameraConfiguration config = cameraConfiguration()
            .width(width)
            .height(height)
//            .brightness(50)
//            .contrast(-30)
//            .saturation(80)
//            .sharpness(100)
//            .stabiliseVideo()
//            .shutterSpeed(10)
//            .iso(4)
//            .exposureMode(ExposureMode.FIREWORKS)
//            .exposureMeteringMode(ExposureMeteringMode.BACKLIT)
//            .exposureCompensation(5)
//            .dynamicRangeCompressionStrength(DynamicRangeCompressionStrength.MAX)
//            .automaticWhiteBalance(AutomaticWhiteBalanceMode.FLUORESCENT)
            .imageEffect(ImageEffect.SKETCH)
//            .flipHorizontally()
//            .flipVertically()
//            .rotation(rotation)
//              .crop(0.25f, 0.25f, 0.5f, 0.5f);
        ;

        try (Camera camera = new Camera(config)) {
            logger.info("created camera " + camera);

            camera.takePicture(new FilePictureCaptureHandler(new File(filename)));
        }

        logger.info("finished");
    }
}