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

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.picam.bindings.internal.MMAL_BUFFER_HEADER_T;
import uk.co.caprica.picam.bindings.internal.MMAL_COMPONENT_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PARAMETER_CAMERA_CONFIG_T;
import uk.co.caprica.picam.bindings.internal.MMAL_POOL_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PORT_T;
import uk.co.caprica.picam.bindings.internal.MMAL_VIDEO_FORMAT_T;

import java.util.concurrent.CountDownLatch;

import static uk.co.caprica.picam.AlignUtils.alignUp;
import static uk.co.caprica.picam.CameraParameterUtils.setAutomaticWhiteBalanceMode;
import static uk.co.caprica.picam.CameraParameterUtils.setBrightness;
import static uk.co.caprica.picam.CameraParameterUtils.setContrast;
import static uk.co.caprica.picam.CameraParameterUtils.setCrop;
import static uk.co.caprica.picam.CameraParameterUtils.setDynamicRangeCompressionStrength;
import static uk.co.caprica.picam.CameraParameterUtils.setExposureCompensation;
import static uk.co.caprica.picam.CameraParameterUtils.setExposureMeteringMode;
import static uk.co.caprica.picam.CameraParameterUtils.setExposureMode;
import static uk.co.caprica.picam.CameraParameterUtils.setImageEffect;
import static uk.co.caprica.picam.CameraParameterUtils.setIso;
import static uk.co.caprica.picam.CameraParameterUtils.setMirror;
import static uk.co.caprica.picam.CameraParameterUtils.setRotation;
import static uk.co.caprica.picam.CameraParameterUtils.setSaturation;
import static uk.co.caprica.picam.CameraParameterUtils.setSharpness;
import static uk.co.caprica.picam.CameraParameterUtils.setShutterSpeed;
import static uk.co.caprica.picam.CameraParameterUtils.setVideoStabilisation;
import static uk.co.caprica.picam.MmalParameterUtils.mmal_port_parameter_set;
import static uk.co.caprica.picam.MmalParameterUtils.mmal_port_parameter_set_boolean;
import static uk.co.caprica.picam.MmalParameterUtils.mmal_port_parameter_set_int32;
import static uk.co.caprica.picam.MmalParameterUtils.mmal_port_parameter_set_uint32;
import static uk.co.caprica.picam.MmalUtils.connectPorts;
import static uk.co.caprica.picam.MmalUtils.createComponent;
import static uk.co.caprica.picam.MmalUtils.destroyComponent;
import static uk.co.caprica.picam.MmalUtils.destroyConnection;
import static uk.co.caprica.picam.MmalUtils.disableComponent;
import static uk.co.caprica.picam.MmalUtils.disablePort;
import static uk.co.caprica.picam.MmalUtils.enableComponent;
import static uk.co.caprica.picam.MmalUtils.getPort;
import static uk.co.caprica.picam.bindings.LibMmal.mmal;
import static uk.co.caprica.picam.bindings.LibMmalUtil.mmalUtil;
import static uk.co.caprica.picam.bindings.MmalParameters.MMAL_PARAMETER_CAMERA_CUSTOM_SENSOR_CONFIG;
import static uk.co.caprica.picam.bindings.MmalParameters.MMAL_PARAMETER_CAMERA_NUM;
import static uk.co.caprica.picam.bindings.MmalParameters.MMAL_PARAMETER_CAPTURE;
import static uk.co.caprica.picam.bindings.internal.MMAL_PARAMETER_CAMERA_CONFIG_TIMESTAMP_MODE_T.MMAL_PARAM_TIMESTAMP_MODE_RESET_STC;
import static uk.co.caprica.picam.bindings.internal.MMAL_STATUS_T.MMAL_SUCCESS;
import static uk.co.caprica.picam.enums.Encoding.OPAQUE;

public final class Camera implements AutoCloseable {

    private static final String MMAL_COMPONENT_DEFAULT_IMAGE_ENCODER = "vc.ril.image_encode";

    private static final String MMAL_COMPONENT_DEFAULT_CAMERA = "vc.ril.camera";

    private static final int MMAL_CAMERA_CAPTURE_PORT = 2;

    private static final int STILLS_FRAME_RATE_NUM = 0;
    private static final int STILLS_FRAME_RATE_DEN = 1;

    private static final int ALIGN_WIDTH = 32;
    private static final int ALIGN_HEIGHT = 16;

    private final Logger logger = LoggerFactory.getLogger(Camera.class);

    private final CameraControlCallback cameraControlCallback = new CameraControlCallback();

    private final CameraConfiguration configuration;

    private MMAL_COMPONENT_T encoderComponent;

    private MMAL_PORT_T encoderInputPort;

    private MMAL_PORT_T encoderOutputPort;

    private MMAL_POOL_T picturePool;

    private MMAL_COMPONENT_T cameraComponent;

    private MMAL_PORT_T cameraCapturePort;

    private Pointer cameraEncoderConnection;

    public Camera(CameraConfiguration configuration) {
        logger.debug("Camera(configuration={})", configuration);

        this.configuration = configuration;

        createEncoder();
        createPicturePool();
        createCamera();
        connectCameraToEncoder();
    }

    public void takePicture(PictureCaptureHandler pictureCaptureHandler) {
        logger.info(">>> Begin Take Picture >>>");

        logger.debug("takePicture()");

        CountDownLatch captureFinishedLatch = new CountDownLatch(1);

        EncoderBufferCallback encoderBufferCallback = new EncoderBufferCallback(pictureCaptureHandler, captureFinishedLatch, picturePool);

        try {
            logger.info("Waiting to capture...");

            Integer delay = configuration.delay();
            logger.debug("delay={}", delay);
            if (delay != null && delay > 0) {
                try {
                    Thread.sleep(delay); // FIXME is this really necessary, Raspistill does it to let the exposure "settle" or somesuch
                }
                catch (InterruptedException e) {
                    logger.error("Interrupted while waiting before capture", e);
                }
            }

            enableEncoderOutput(encoderBufferCallback);
            sendBuffersToEncoder();

            startCapture();

            pictureCaptureHandler.begin();

            try {
                logger.debug("wait for capture to complete");
                captureFinishedLatch.await(); // FIXME timeout version?
                logger.info("Capture completed");
            }
            catch (InterruptedException e) {
                logger.warn("Interrupted waiting for capture to finish", e);
            }
        }
        catch (Exception e) {
            logger.error("Capture failure", e);
        }
        finally {
            try {
                pictureCaptureHandler.end();
            }
            catch (Exception e) {
                logger.error("Callback failure after capture finished", e);
            }

            disableEncoderOutputPort();
            encoderBufferCallback = null;
        }

        logger.info("<<< End Take Picture <<<");
    }

    @Override
    public void close() throws Exception {
        logger.debug("close()");

        // FIXME something wrong with clean-up
        //    1. a warning will be logged complaining about a port already being disabled
        //    2. occasionally there is a race apparent, a call to disable returns success yet the resource reports it is
        //       still enabled
        // might be fixed by port.isEnabled() and the new read()?

        disableEncoderOutputPort();

//        destroyConnection(cameraEncoderConnection);

        disableComponent(encoderComponent);
        disableComponent(cameraComponent);

        mmalUtil.mmal_port_pool_destroy(encoderOutputPort, picturePool);

        destroyComponent(encoderComponent);
        destroyComponent(cameraComponent);
    }

    private void createEncoder() {
        logger.debug("createEncoder()");

        encoderComponent = createComponent(MMAL_COMPONENT_DEFAULT_IMAGE_ENCODER);

        encoderInputPort = getPort(encoderComponent.input.getPointer(0));
        logger.trace("encoderInputPort={}", encoderInputPort);

        encoderOutputPort = getPort(encoderComponent.output.getPointer(0));
        logger.trace("encoderOutputPort={}", encoderOutputPort);

        mmal.mmal_format_copy(encoderOutputPort.format, encoderInputPort.format);

        encoderOutputPort.format.encoding = configuration.encoding().value();
        encoderOutputPort.buffer_size = Math.max(encoderOutputPort.buffer_size_recommended, encoderOutputPort.buffer_size_min);
        encoderOutputPort.buffer_num = Math.max(encoderOutputPort.buffer_num_recommended, encoderOutputPort.buffer_num_min);
        encoderOutputPort.write();

        logger.trace("encoderOutputPort={}", encoderOutputPort);

        if (mmal.mmal_port_format_commit(encoderOutputPort) != MMAL_SUCCESS) {
            throw new RuntimeException("Failed to commit encoder output port format");
        }

        enableComponent(encoderComponent);
    }

    private void createPicturePool() {
        logger.debug("createPicturePool()");

        picturePool = mmalUtil.mmal_port_pool_create(encoderOutputPort, encoderOutputPort.buffer_num, encoderOutputPort.buffer_size);

        logger.trace("picturePool={}", picturePool);

        if (picturePool == null) {
            throw new RuntimeException("Failed to create encoder picture pool");
        }
    }

    private void createCamera() {
        logger.debug("createCamera()");

        cameraComponent = createComponent(MMAL_COMPONENT_DEFAULT_CAMERA);

        mmal_port_parameter_set_int32(cameraComponent.control, MMAL_PARAMETER_CAMERA_NUM, 0);
        mmal_port_parameter_set_uint32(cameraComponent.control, MMAL_PARAMETER_CAMERA_CUSTOM_SENSOR_CONFIG, 0);

        Pointer[] pOutputs = cameraComponent.output.getPointerArray(0, cameraComponent.output_num);
        cameraCapturePort = new MMAL_PORT_T(pOutputs[MMAL_CAMERA_CAPTURE_PORT]);
        cameraCapturePort.read();

        logger.trace("cameraCapturePort={}", cameraCapturePort);

        mmal.mmal_port_enable(cameraComponent.control, cameraControlCallback);

        applyCameraControlConfiguration();
        applyCameraConfiguration();
        applyCameraCapturePortFormat();

        enableComponent(cameraComponent);
    }

    private void applyCameraControlConfiguration() {
        logger.debug("applyCameraControlConfiguration()");

        MMAL_PARAMETER_CAMERA_CONFIG_T config = new MMAL_PARAMETER_CAMERA_CONFIG_T();
        config.max_stills_w = configuration.width();
        config.max_stills_h = configuration.height();
        config.stills_yuv422 = 0;
        config.one_shot_stills = 1;
        // Preview configuration must be set to something reasonable, even though preview is not used
        config.max_preview_video_w = 320;
        config.max_preview_video_h = 240;
        config.num_preview_video_frames = 3;
        config.stills_capture_circular_buffer_height = 0;
        config.fast_preview_resume = 0;
        config.use_stc_timestamp = MMAL_PARAM_TIMESTAMP_MODE_RESET_STC;

        logger.trace("config={}", config);

        int result = mmal_port_parameter_set(cameraComponent.control, config);
        logger.debug("result={}", result);

        if (result != MMAL_SUCCESS) {
            throw new RuntimeException("Failed to set camera control port configuration");
        }
    }

    private void applyCameraConfiguration() {
        logger.debug("applyCameraConfiguration()");

        setBrightness(cameraComponent, configuration.brightness());
        setContrast(cameraComponent, configuration.contrast());
        setSaturation(cameraComponent, configuration.saturation());
        setSharpness(cameraComponent, configuration.sharpness());
        setVideoStabilisation(cameraComponent, configuration.videoStabilisation());
        setShutterSpeed(cameraComponent, configuration.shutterSpeed());
        setIso(cameraComponent, configuration.iso());
        setExposureMode(cameraComponent, configuration.exposureMode());
        setExposureMeteringMode(cameraComponent, configuration.exposureMeteringMode());
        setExposureCompensation(cameraComponent, configuration.exposureCompensation());
        setDynamicRangeCompressionStrength(cameraComponent, configuration.dynamicRangeCompressionStrength());
        setAutomaticWhiteBalanceMode(cameraComponent, configuration.automaticWhiteBalanceMode());
        setImageEffect(cameraComponent, configuration.imageEffect());
        setMirror(cameraComponent, configuration.mirror());
        setRotation(cameraComponent, configuration.rotation());
        setCrop(cameraComponent, configuration.crop());
    }

    private void applyCameraCapturePortFormat() {
        logger.debug("applyCameraCapturePortFormat()");

        cameraCapturePort.format.encoding = OPAQUE.value();
        cameraCapturePort.format.es.video.width = alignUp(configuration.width(), ALIGN_WIDTH);
        cameraCapturePort.format.es.video.height = alignUp(configuration.height(), ALIGN_HEIGHT);
        cameraCapturePort.format.es.video.crop.x = 0;
        cameraCapturePort.format.es.video.crop.y = 0;
        cameraCapturePort.format.es.video.crop.width = configuration.width();
        cameraCapturePort.format.es.video.crop.height = configuration.height();
        cameraCapturePort.format.es.video.frame_rate.num = STILLS_FRAME_RATE_NUM;
        cameraCapturePort.format.es.video.frame_rate.den = STILLS_FRAME_RATE_DEN;

        cameraCapturePort.format.es.setType(MMAL_VIDEO_FORMAT_T.class);
        cameraCapturePort.write();

        logger.trace("format={}", cameraCapturePort.format.es.video);

        int result = mmal.mmal_port_format_commit(cameraCapturePort);
        logger.debug("result={}", result);

        if (result != MMAL_SUCCESS) {
            throw new RuntimeException("Failed to commit camera capture port format");
        }
    }

    private void connectCameraToEncoder() {
        logger.debug("connectCameraToEncoder()");

        PointerByReference pConnection = new PointerByReference();
        connectPorts(cameraCapturePort, encoderInputPort, pConnection);
        cameraEncoderConnection = pConnection.getValue();

        logger.trace("cameraEncoderConnection={}", cameraEncoderConnection);
    }

    private void enableEncoderOutput(EncoderBufferCallback encoderBufferCallback) {
        logger.debug("enableEncoderOutput()");

        int result = mmal.mmal_port_enable(encoderOutputPort, encoderBufferCallback);
        logger.debug("result={}", result);

        if (result != MMAL_SUCCESS) {
            throw new RuntimeException("Failed to enable encoder output port");
        }
    }

    private void sendBuffersToEncoder() {
        logger.debug("sendBuffersToEncoder()");

        int bufferCount = mmal.mmal_queue_length(picturePool.queue);
        logger.debug("bufferCount={}", bufferCount);

        for (int i = 0; i < bufferCount; i++) {
            MMAL_BUFFER_HEADER_T buffer = mmal.mmal_queue_get(picturePool.queue);
            logger.trace("buffer={}", buffer);

            if (buffer == null) {
                throw new RuntimeException(String.format("Failed to get buffer %d from queue", i));
            }

            int result = mmal.mmal_port_send_buffer(encoderOutputPort, buffer);
            logger.debug("result={}", result);

            if (result != MMAL_SUCCESS) {
                throw new RuntimeException(String.format("Failed to send buffer %d to encoder output port", i));
            }
        }
    }

    private void startCapture() {
        logger.debug("startCapture()");

        int result = mmal_port_parameter_set_boolean(cameraCapturePort, MMAL_PARAMETER_CAPTURE, 1);
        logger.debug("result={}", result);

        if (result != MMAL_SUCCESS) {
            throw new RuntimeException("Failed to start capture");
        }

        logger.info("Capture started");
    }

    private void disableEncoderOutputPort() {
        logger.debug("disableEncoderOutputPort()");

        disablePort(encoderOutputPort);
    }
}
