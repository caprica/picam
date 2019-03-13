/*
 * This file is part of picam.
 *
 * picam is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * picam is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY), without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with picam.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2016-2019 Caprica Software Limited.
 */

package uk.co.caprica.picam.enums;

public enum ImageEffect {

    NONE(0),
    NEGATIVE(1),
    SOLARIZE(2),
    POSTERIZE(3),
    WHITEBOARD(4),
    BLACKBOARD(5),
    SKETCH(6),
    DENOISE(7),
    EMBOSS(8),
    OILPAINT(9),
    HATCH(10),
    GPEN(11),
    PASTEL(12),
    WATERCOLOUR(13),
    FILM(14),
    BLUR(15),
    SATURATION(16),
    COLOURSWAP(17),
    WASHEDOUT(18),
    POSTERISE(19),
    COLOURPOINT(20),
    COLOURBALANCE(21),
    CARTOON(22),
    DEINTERLACE_DOUBLE(23),
    DEINTERLACE_ADV(24),
    DEINTERLACE_FAST(25),
    MAX(0x7fffffff);

    private final int value;

    ImageEffect(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
