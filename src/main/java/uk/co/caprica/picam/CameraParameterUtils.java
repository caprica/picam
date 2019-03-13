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

import com.sun.jna.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.picam.bindings.internal.MMAL_COMPONENT_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PARAMETER_AWBMODE_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PARAMETER_AWB_GAINS_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PARAMETER_COLOURFX_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PARAMETER_DRC_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PARAMETER_EXPOSUREMETERINGMODE_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PARAMETER_EXPOSUREMODE_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PARAMETER_FPS_RANGE_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PARAMETER_IMAGEFX_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PARAMETER_INPUT_CROP_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PARAMETER_MIRROR_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PARAMETER_STEREOSCOPIC_MODE_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PORT_T;
import uk.co.caprica.picam.bindings.internal.MMAL_RATIONAL_T;
import uk.co.caprica.picam.enums.AutomaticWhiteBalanceMode;
import uk.co.caprica.picam.enums.DynamicRangeCompressionStrength;
import uk.co.caprica.picam.enums.ExposureMeteringMode;
import uk.co.caprica.picam.enums.ExposureMode;
import uk.co.caprica.picam.enums.ImageEffect;
import uk.co.caprica.picam.enums.Mirror;
import uk.co.caprica.picam.enums.StereoscopicMode;

import java.awt.geom.Rectangle2D;

import static uk.co.caprica.picam.MmalParameterUtils.mmal_port_parameter_set;
import static uk.co.caprica.picam.MmalParameterUtils.mmal_port_parameter_set_boolean;
import static uk.co.caprica.picam.MmalParameterUtils.mmal_port_parameter_set_int32;
import static uk.co.caprica.picam.MmalParameterUtils.mmal_port_parameter_set_rational;
import static uk.co.caprica.picam.MmalParameterUtils.mmal_port_parameter_set_uint32;
import static uk.co.caprica.picam.bindings.LibMmal.mmal;
import static uk.co.caprica.picam.bindings.MmalParameters.MMAL_PARAMETER_BRIGHTNESS;
import static uk.co.caprica.picam.bindings.MmalParameters.MMAL_PARAMETER_CONTRAST;
import static uk.co.caprica.picam.bindings.MmalParameters.MMAL_PARAMETER_EXPOSURE_COMP;
import static uk.co.caprica.picam.bindings.MmalParameters.MMAL_PARAMETER_ISO;
import static uk.co.caprica.picam.bindings.MmalParameters.MMAL_PARAMETER_ROTATION;
import static uk.co.caprica.picam.bindings.MmalParameters.MMAL_PARAMETER_SATURATION;
import static uk.co.caprica.picam.bindings.MmalParameters.MMAL_PARAMETER_SHARPNESS;
import static uk.co.caprica.picam.bindings.MmalParameters.MMAL_PARAMETER_SHUTTER_SPEED;
import static uk.co.caprica.picam.bindings.MmalParameters.MMAL_PARAMETER_VIDEO_STABILISATION;
import static uk.co.caprica.picam.bindings.internal.MMAL_STATUS_T.MMAL_SUCCESS;

// FIXME handle null-checks were applicable, e.g. boxing on boolean ?

class CameraParameterUtils {

    private static final Logger logger = LoggerFactory.getLogger(CameraParameterUtils.class);

    private static final int MMAL_CAMERA_CAPTURE_PORT = 2;

    private CameraParameterUtils() {
    }

    static void setStereoscopicMode(MMAL_COMPONENT_T camera, StereoscopicMode stereoscopicMode, Boolean decimate, Boolean swapEyes) {
        logger.debug("setStereoscopicMode(stereoscopicMode={},decimate={},swapEyes={})", stereoscopicMode, decimate, swapEyes);

        MMAL_PARAMETER_STEREOSCOPIC_MODE_T param = new MMAL_PARAMETER_STEREOSCOPIC_MODE_T();
        if (stereoscopicMode != StereoscopicMode.NONE) {
            param.mode = stereoscopicMode.value();
            param.decimate = decimate ? 1 : 0;
            param.swap_eyes = swapEyes ? 1 : 0;
        }
        param.write();

        checkResult("Stereoscopic Mode", mmal.mmal_port_parameter_set(getCameraCapturePort(camera), param.hdr));
    }

    static void setBrightness(MMAL_COMPONENT_T camera, Integer brightness) {
        logger.debug("setBrightness(brightness={})", brightness);

        if (brightness != null) {
            checkRange("Brightness", 0, 100, brightness);

            MMAL_RATIONAL_T value = new MMAL_RATIONAL_T(brightness, 100);
            checkResult("Brightness", mmal_port_parameter_set_rational(camera.control, MMAL_PARAMETER_BRIGHTNESS, value));
        }
    }

    static void setContrast(MMAL_COMPONENT_T camera, Integer contrast) {
        logger.debug("setContrast(contrast={})", contrast);

        if (contrast != null) {
            checkRange("Contrast", -100, 100, contrast);

            MMAL_RATIONAL_T value = new MMAL_RATIONAL_T(contrast, 100);
            checkResult("Contrast", mmal_port_parameter_set_rational(camera.control, MMAL_PARAMETER_CONTRAST, value));
        }
    }

    static void setSaturation(MMAL_COMPONENT_T camera, Integer saturation) {
        logger.debug("setSaturation(saturation={})", saturation);

        if (saturation != null) {
            checkRange("Saturation", -100, 100, saturation);

            MMAL_RATIONAL_T value = new MMAL_RATIONAL_T(saturation, 100);
            checkResult("Saturation", mmal_port_parameter_set_rational(camera.control, MMAL_PARAMETER_SATURATION, value));
        }
    }

    static void setSharpness(MMAL_COMPONENT_T camera, Integer sharpness) {
        logger.debug("setSharpness(sharpness={})", sharpness);

        if (sharpness != null) {
            checkRange("Sharpness", -100, 100, sharpness);

            MMAL_RATIONAL_T value = new MMAL_RATIONAL_T(sharpness, 100);
            checkResult("Sharpness", mmal_port_parameter_set_rational(camera.control, MMAL_PARAMETER_SHARPNESS, value));
        }
    }

    static void setVideoStabilisation(MMAL_COMPONENT_T camera, Boolean videoStabilisation) {
        logger.debug("setVideoStabilisation(videoStabilisation={})", videoStabilisation);

        if (videoStabilisation != null) {
            checkResult("Video Stabilisation", mmal_port_parameter_set_boolean(camera.control, MMAL_PARAMETER_VIDEO_STABILISATION, videoStabilisation ? 1 : 0));
        }
    }

    static void setShutterSpeed(MMAL_COMPONENT_T camera, Integer shutterSpeed) {
        logger.debug("setShutterSpeed(shutterSpeed={})", shutterSpeed);

        if (shutterSpeed != null) {
            checkResult("Shutter Speed", mmal_port_parameter_set_uint32(camera.control, MMAL_PARAMETER_SHUTTER_SPEED, shutterSpeed));
        }
    }

    static void setIso(MMAL_COMPONENT_T camera, Integer iso) {
        logger.debug("setIso(iso={})", iso);

        if (iso != null) {
            checkResult("ISO", mmal_port_parameter_set_uint32(camera.control, MMAL_PARAMETER_ISO, iso));
        }
    }

    static void setExposureMode(MMAL_COMPONENT_T camera, ExposureMode exposureMode) {
        logger.debug("setExposureMode(exposureMode={})", exposureMode);

        if (exposureMode != null) {
            MMAL_PARAMETER_EXPOSUREMODE_T param = new MMAL_PARAMETER_EXPOSUREMODE_T();
            param.value = exposureMode.value();

            checkResult("Exposure Mode", mmal_port_parameter_set(camera.control, param));
        }
    }

    static void setExposureMeteringMode(MMAL_COMPONENT_T camera, ExposureMeteringMode exposureMeteringMode) {
        logger.debug("setExposureMeteringMode(exposureMeteringMode={})", exposureMeteringMode);

        if (exposureMeteringMode != null) {
            MMAL_PARAMETER_EXPOSUREMETERINGMODE_T param = new MMAL_PARAMETER_EXPOSUREMETERINGMODE_T.ByReference();
            param.value = exposureMeteringMode.value();

            checkResult("Exposure Metering Mode", mmal_port_parameter_set(camera.control, param));
        }
    }

    static void setExposureCompensation(MMAL_COMPONENT_T camera, Integer exposureCompensation) {
        logger.debug("setExposureCompensation(exposureCompensation={})", exposureCompensation);

        if (exposureCompensation != null) {
            checkRange("Exposure Compensation", -10, 10, exposureCompensation);

            checkResult("Exposure Compensation", mmal_port_parameter_set_int32(camera.control, MMAL_PARAMETER_EXPOSURE_COMP, exposureCompensation));
        }
    }

    static void setDynamicRangeCompressionStrength(MMAL_COMPONENT_T camera, DynamicRangeCompressionStrength dynamicRangeCompressionStrength) {
        logger.debug("setDynamicRangeCompressionStrength(dynamicRangeCompressionStrength={})", dynamicRangeCompressionStrength);

        if (dynamicRangeCompressionStrength != null) {
            MMAL_PARAMETER_DRC_T param = new MMAL_PARAMETER_DRC_T();
            param.strength = dynamicRangeCompressionStrength.value();

            checkResult("Dynamic Range Compression Strength", mmal_port_parameter_set(camera.control, param));
        }
    }

    static void setAutomaticWhiteBalanceMode(MMAL_COMPONENT_T camera, AutomaticWhiteBalanceMode automaticWhiteBalanceMode) {
        logger.debug("setAutomaticWhiteBalanceMode(automaticWhiteBalanceMode={})", automaticWhiteBalanceMode);

        if (automaticWhiteBalanceMode != null) {
            MMAL_PARAMETER_AWBMODE_T param = new MMAL_PARAMETER_AWBMODE_T();
            param.value = automaticWhiteBalanceMode.value();

            checkResult("Automatic White Balance Mode", mmal_port_parameter_set(camera.control, param));
        }
    }

    static void setAutomaticWhiteBalanceGains(MMAL_COMPONENT_T camera, Float redGain, Float blueGain) {
        logger.debug("setAutomaticWhiteBalanceGains(redGain={},blueGain={})", redGain, blueGain);

        if (redGain != null || blueGain != null) {
            MMAL_PARAMETER_AWB_GAINS_T param = new MMAL_PARAMETER_AWB_GAINS_T();

            if (redGain != null) {
                param.r_gain.num = (int) (redGain * 65536);
                param.r_gain.den = 65536;
            }

            if (blueGain != null) {
                param.b_gain.num = (int) (blueGain * 65536);
                param.b_gain.den = 65536;
            }

            checkResult("Automatic White Balance Gains", mmal_port_parameter_set(camera.control, param));
        }
    }

    static void setImageEffect(MMAL_COMPONENT_T camera, ImageEffect imageEffect) {
        logger.debug("setImageEffect(imageEffect={})", imageEffect);

        if (imageEffect != null) {
            MMAL_PARAMETER_IMAGEFX_T param = new MMAL_PARAMETER_IMAGEFX_T();
            param.value = imageEffect.value();

            checkResult("Image Effect", mmal_port_parameter_set(camera.control, param));
        }
    }

    static void setMirror(MMAL_COMPONENT_T camera, Mirror mirror) {
        logger.debug("setMirror(mirror={})", mirror);

        if (mirror != null) {
            MMAL_PARAMETER_MIRROR_T param = new MMAL_PARAMETER_MIRROR_T();
            param.value = mirror.value();

            checkResult("Mirror", mmal_port_parameter_set(getCameraCapturePort(camera), param));
        }
    }

    static void setRotation(MMAL_COMPONENT_T camera, Integer rotation) {
        logger.debug("setRotation(rotation={})", rotation);

        if (rotation != null) {
            rotation = ((rotation % 360) / 90) * 90;
            logger.debug("rotation={}", rotation);

            checkResult("Rotation", mmal_port_parameter_set_int32(getCameraCapturePort(camera), MMAL_PARAMETER_ROTATION, rotation));
        }
    }

    static void setCrop(MMAL_COMPONENT_T camera, float x, float y, float width, float height) {
        logger.debug("setCrop(x={},y={},width={},height={})", x, y, width, height);

        MMAL_PARAMETER_INPUT_CROP_T param = new MMAL_PARAMETER_INPUT_CROP_T();
        param.rect.x = (int)(65536 * x);
        param.rect.y = (int)(65536 * y);
        param.rect.width = (int)(65536 * width);
        param.rect.height = (int)(65536 * height);

        checkResult("Crop", mmal_port_parameter_set(camera.control, param));
    }

    static void setCrop(MMAL_COMPONENT_T camera, Rectangle2D.Float crop) {
        logger.debug("setCrop(crop={})", crop);

        setCrop(camera, crop.x, crop.y, crop.width, crop.height);
    }

    static void setColourEffect(MMAL_COMPONENT_T camera, Boolean enable, Integer u, Integer v) {
        logger.debug("setColourEffect(enable={},u={},v={})", enable, u, v);

        if (enable != null) {
            MMAL_PARAMETER_COLOURFX_T param = new MMAL_PARAMETER_COLOURFX_T();
            param.enable = enable ? 1 : 0;
            param.u = u;
            param.v = v;

            checkResult("Colour Effect", mmal.mmal_port_parameter_set(camera.control, param.hdr));
        }
    }

    static void setFpsRange(MMAL_COMPONENT_T camera, int lowNum, int lowDen, int highNum, int highDen) {
        logger.debug("setFpsRange(lowNum={},lowDen={},highNum={},highDen={})", lowNum, lowDen, highNum, highDen);

        MMAL_PARAMETER_FPS_RANGE_T param = new MMAL_PARAMETER_FPS_RANGE_T();
        param.fps_low.num = lowNum;
        param.fps_low.den = lowDen;
        param.fps_high.num = highNum;
        param.fps_high.den = highDen;

        checkResult("FPS Range", mmal.mmal_port_parameter_set(getCameraCapturePort(camera), param.hdr));
    }

    private static void checkRange(String name, Integer min, Integer max, Integer value) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(String.format("%s must be in range %d to %d", name, min, max));
        }
    }

    private static void checkResult(String name, Integer result) {
        if (result != MMAL_SUCCESS) {
            throw new RuntimeException(String.format("Failed to set parameter %s, result %d", name, result));
        }
    }

    private static MMAL_PORT_T getCameraCapturePort(MMAL_COMPONENT_T camera) {
        logger.debug("getCameraCapturePort()");

        Pointer[] pOutputs = camera.output.getPointerArray(0, camera.output_num);

        MMAL_PORT_T port = new MMAL_PORT_T(pOutputs[MMAL_CAMERA_CAPTURE_PORT]);
        port.read();

        logger.trace("port={}", port);

        return port;
    }
}
