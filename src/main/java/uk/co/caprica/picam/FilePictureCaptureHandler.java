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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class FilePictureCaptureHandler implements PictureCaptureHandler<File> {

    private final File file;

    private BufferedOutputStream out;

    public FilePictureCaptureHandler(File file) {
        this.file = file;
    }

    public FilePictureCaptureHandler(String file) {
        this(new File(file));
    }

    @Override
    public void begin() throws Exception {
        out = new BufferedOutputStream(new FileOutputStream(file));
    }

    @Override
    public int pictureData(byte[] data) throws Exception {
        out.write(data);
        return data.length;
    }

    @Override
    public void end() throws Exception {
        if (out != null) {
            out.flush();
            out.close();
            out = null;
        }
    }

    @Override
    public File result() {
        return file;
    }

}
