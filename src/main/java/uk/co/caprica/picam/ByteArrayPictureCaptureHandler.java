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

import java.io.ByteArrayOutputStream;

/**
 * Implementation of a picture capture handler that stores the image data in a byte array.
 */
public class ByteArrayPictureCaptureHandler implements PictureCaptureHandler<byte[]> {

    private final Integer initialSize;

    private ByteArrayOutputStream out;

    /**
     * Create a picture capture handler.
     */
    public ByteArrayPictureCaptureHandler() {
        this.initialSize = null;
    }

    /**
     * Create a picture capture handler.
     *
     * @param initialSize initial size of the byte buffer, it will be automatically extended as needed
     */
    public ByteArrayPictureCaptureHandler(int initialSize) {
        this.initialSize = initialSize;
    }

    @Override
    public void begin() throws Exception {
        out = initialSize != null ? new ByteArrayOutputStream(initialSize) : new ByteArrayOutputStream();
    }

    @Override
    public int pictureData(byte[] data) throws Exception {
        out.write(data);
        return data.length;
    }

    @Override
    public void end() throws Exception {
    }

    @Override
    public byte[] result() {
        return out.toByteArray();
    }

}
