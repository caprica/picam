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

import java.io.ByteArrayOutputStream;

public class ByteArrayPictureCaptureHandler implements PictureCaptureHandler<ByteArrayOutputStream> {

    private ByteArrayOutputStream out;

    @Override
    public void begin() throws Exception {
        out = new ByteArrayOutputStream();
    }

    @Override
    public void pictureData(byte[] data) throws Exception {
        out.write(data);
    }

    @Override
    public void end() throws Exception {
    }

    @Override
    public ByteArrayOutputStream result() {
        return out;
    }
}
