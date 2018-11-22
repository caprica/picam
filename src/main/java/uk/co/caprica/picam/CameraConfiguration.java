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

import uk.co.caprica.picam.enums.Encoding;
import uk.co.caprica.picam.enums.ExposureMode;
import uk.co.caprica.picam.enums.AutomaticWhiteBalanceMode;
import uk.co.caprica.picam.enums.DynamicRangeCompressionStrength;
import uk.co.caprica.picam.enums.ExposureMeteringMode;
import uk.co.caprica.picam.enums.ImageEffect;
import uk.co.caprica.picam.enums.Mirror;
import uk.co.caprica.picam.enums.StereoscopicMode;

import java.awt.geom.Rectangle2D;

import static uk.co.caprica.picam.enums.Encoding.PNG;

public final class CameraConfiguration {

    private static final Integer DEFAULT_WIDTH = 2592;

    private static final Integer DEFAULT_HEIGHT = 1944;

    private static final Integer DEFAULT_DELAY = 5;

    private Integer width = DEFAULT_WIDTH;

    private Integer height = DEFAULT_HEIGHT;

    private Encoding encoding = PNG;

    private Integer quality;

    private StereoscopicMode stereoscopicMode;

    private Integer brightness;

    private Integer contrast;

    private Integer saturation;

    private Integer sharpness;

    private Boolean videoStabilisation;

    private Integer shutterSpeed;

    private Integer iso;

    private ExposureMode exposureMode;

    private ExposureMeteringMode exposureMeteringMode;

    private Integer exposureCompensation;

    private DynamicRangeCompressionStrength dynamicRangeCompressionStrength;

    private AutomaticWhiteBalanceMode automaticWhiteBalanceMode;

    private float automaticWhiteBalanceRedGain;

    private float automaticWhiteBalanceBlueGain;

    private ImageEffect imageEffect;

    private Mirror mirror;

    private Integer rotation;

    private Rectangle2D.Float crop = new Rectangle2D.Float(0.f, 0.f, 1.f, 1.f);

    private Integer delay = DEFAULT_DELAY;

//    private Integer imageEffectsParameters;
//    private Integer colourEffects;

    private CameraConfiguration() {
    }

    public static CameraConfiguration cameraConfiguration() {
        return new CameraConfiguration();
    }

    public CameraConfiguration width(Integer width) {
        this.width = width;
        return this;
    }

    public CameraConfiguration height(Integer height) {
        this.height = height;
        return this;
    }

    public CameraConfiguration size(Integer width, Integer height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public CameraConfiguration encoding(Encoding encoding) {
        this.encoding = encoding;
        return this;
    }

    public CameraConfiguration quality(Integer quality) {
        if (quality < 0 || quality > 100) {
            throw new IllegalArgumentException("Quality must be in the range 0 to 100");
        }
        this.quality = quality;
        return this;
    }

    public CameraConfiguration stereoscopicMode(StereoscopicMode stereoscopicMode) {
        this.stereoscopicMode = stereoscopicMode;
        return this;
    }

    public CameraConfiguration brightness(Integer brightness) {
        if (brightness < 0 || brightness > 100) {
            throw new IllegalArgumentException("Brightness must be in the range 0 to 100");
        }
        this.brightness = brightness;
        return this;
    }

    public CameraConfiguration contrast(Integer contrast) {
        if (contrast < -100 || contrast > 100) {
            throw new IllegalArgumentException("Contrast must be in the range -100 to 100");
        }
        this.contrast = contrast;
        return this;
    }

    public CameraConfiguration saturation(Integer saturation) {
        if (saturation < -100 || saturation > 100) {
            throw new IllegalArgumentException("Saturation must be in the range -100 to 100");
        }
        this.saturation = saturation;
        return this;
    }

    public CameraConfiguration sharpness(Integer sharpness) {
        if (sharpness < -100 || sharpness > 100) {
            throw new IllegalArgumentException("Sharpness must be in the range -100 to 100");
        }
        this.sharpness = sharpness;
        return this;
    }

    // FIXME naming conflict videoStabilisation? can i do anything better rather than get/set/is ?
    public CameraConfiguration stabiliseVideo() {
        this.videoStabilisation = true;
        return this;
    }

    public CameraConfiguration shutterSpeed(Integer shutterSpeed) {
        this.shutterSpeed = shutterSpeed;
        return this;
    }

    public CameraConfiguration iso(Integer iso) {
        this.iso = iso;
        return this;
    }

    public CameraConfiguration exposureMode(ExposureMode exposureMode) {
        this.exposureMode = exposureMode;
        return this;
    }

    public CameraConfiguration exposureMeteringMode(ExposureMeteringMode exposureMeteringMode) {
        this.exposureMeteringMode = exposureMeteringMode;
        return this;
    }

    public CameraConfiguration exposureCompensation(Integer exposureCompensation) {
        if (exposureCompensation < -10 || exposureCompensation > 10) {
            throw new IllegalArgumentException("Exposure Compensation must be in the range -10 to 10");
        }
        this.exposureCompensation = exposureCompensation;
        return this;
    }

    public CameraConfiguration dynamicRangeCompressionStrength(DynamicRangeCompressionStrength dynamicRangeCompressionStrength) {
        this.dynamicRangeCompressionStrength = dynamicRangeCompressionStrength;
        return this;
    }

    public CameraConfiguration automaticWhiteBalance(AutomaticWhiteBalanceMode automaticWhiteBalanceMode) {
        this.automaticWhiteBalanceMode = automaticWhiteBalanceMode;
        return this;
    }

    public CameraConfiguration automaticWhiteBalanceRedGain(float automaticWhiteBalanceRedGain) {
        this.automaticWhiteBalanceRedGain = automaticWhiteBalanceRedGain;
        return this;
    }

    public CameraConfiguration automaticWhiteBalanceBlueGain(float automaticWhiteBalanceBlueGain) {
        this.automaticWhiteBalanceBlueGain = automaticWhiteBalanceBlueGain;
        return this;
    }

    public CameraConfiguration automaticWhiteBalanceGain(float automaticWhiteBalanceRedGain, float automaticWhiteBalanceBlueGain) {
        this.automaticWhiteBalanceRedGain = automaticWhiteBalanceRedGain;
        this.automaticWhiteBalanceBlueGain = automaticWhiteBalanceBlueGain;
        return this;
    }

    public CameraConfiguration imageEffect(ImageEffect imageEffect) {
        this.imageEffect = imageEffect;
        return this;
    }

    public CameraConfiguration mirror(Mirror mirror) {
        this.mirror = mirror;
        return this;
    }

    public CameraConfiguration rotation(Integer rotation) {
        this.rotation = rotation;
        return this;
    }

    public CameraConfiguration crop(float x, float y, float width, float height) {
        this.crop.setRect(x, y, width, height);
        return this;
    }

    public CameraConfiguration delay(Integer delay) {
        this.delay = delay;
        return this;
    }

    public Camera camera() {
        return new Camera(this);
    }

    public Integer width() {
        return width;
    }

    public Integer height() {
        return height;
    }

    public Encoding encoding() {
        return encoding;
    }

    public Integer quality() {
        return quality;
    }

    public Integer brightness() {
        return brightness;
    }

    public Integer contrast() {
        return contrast;
    }

    public Integer saturation() {
        return saturation;
    }

    public Integer sharpness() {
        return sharpness;
    }

    public Boolean videoStabilisation() {
        return videoStabilisation;
    }

    public Integer shutterSpeed() {
        return shutterSpeed;
    }

    public Integer iso() {
        return iso;
    }

    public ExposureMode exposureMode() {
        return exposureMode;
    }

    public ExposureMeteringMode exposureMeteringMode() {
        return exposureMeteringMode;
    }

    public Integer exposureCompensation() {
        return exposureCompensation;
    }

    public DynamicRangeCompressionStrength dynamicRangeCompressionStrength() {
        return dynamicRangeCompressionStrength;
    }

    public AutomaticWhiteBalanceMode automaticWhiteBalanceMode() {
        return automaticWhiteBalanceMode;
    }

    public ImageEffect imageEffect() {
        return imageEffect;
    }

    public Mirror mirror() {
        return mirror;
    }

    public Integer rotation() {
        return rotation;
    }

    public Rectangle2D.Float crop() {
        return crop;
    }

    public Integer delay() {
        return delay;
    }
}
