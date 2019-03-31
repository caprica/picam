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

/**
 * A simple picture capture handler implementation that saves images to disk with sequentially
 * numbered filenames.
 */
public class SequentialFilePictureCaptureHandler implements PictureCaptureHandler<File> {

    // Buffer size, somewhat arbitrary (default is 32k)
    private static final int BUFFER_SIZE = 1024 * 32;

    private final String pattern;

    private int number;

    private File file;

    private BufferedOutputStream out;

    public SequentialFilePictureCaptureHandler(String pattern) {
        this(pattern, 1);
    }

    public SequentialFilePictureCaptureHandler(String pattern, int initial) {
        this.pattern = pattern;
        this.number = initial;
    }

    @Override
    public void begin() throws Exception {
        file = new File(String.format(pattern, number++));
        out = new BufferedOutputStream(new FileOutputStream(file), BUFFER_SIZE);
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
