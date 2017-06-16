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

package uk.co.caprica.picam.enums;

public enum Encoding {

    BMP("BMP "),
    GIF("GIF "),
    I420("I420"),
    JPEG("JPEG"),
    PNG("PNG "),

    OPAQUE("OPQV");

    private final int value;

    Encoding(String encoding) {
        this.value = fourCC(encoding);
    }

    public int value() {
        return value;
    }

    private static int fourCC(String value) {
        return value.charAt(0) | (value.charAt(1) << 8) | (value.charAt(2) << 16) | (value.charAt(3) << 24);
    }
}
