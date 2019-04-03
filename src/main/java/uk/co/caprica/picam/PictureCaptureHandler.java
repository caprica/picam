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

/**
 * Specification for a component that accepts and processes picture capture data.
 *
 * @param <T> type of result produced by the picture capture handler
 */
public interface PictureCaptureHandler<T> {

    /**
     * Begin a new capture.
     * <p>
     * This is used, for example, to open a new output stream.
     *
     * @throws Exception if a general error occurs
     */
    void begin() throws Exception;

    /**
     * Process picture capture data.
     *
     * @param data picture data
     * @return number of bytes processed, if this does not equal the size of the supplied data the capture will be aborted in error
     * @throws Exception if a general error occurs
     */
    int pictureData(byte[] data) throws Exception;

    /**
     * End a capture.
     * <p>
     * This is used, for example, to close a previously opened new output stream.
     *
     * @throws Exception if a general error occurs
     */
    void end() throws Exception;

    T result();

}
