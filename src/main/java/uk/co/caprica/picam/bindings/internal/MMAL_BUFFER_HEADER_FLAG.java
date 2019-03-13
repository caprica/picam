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

package uk.co.caprica.picam.bindings.internal;

public interface MMAL_BUFFER_HEADER_FLAG {

    int MMAL_BUFFER_HEADER_FLAG_EOS                    = (1 << 0);
    int MMAL_BUFFER_HEADER_FLAG_FRAME_START            = (1 << 1);
    int MMAL_BUFFER_HEADER_FLAG_FRAME_END              = (1 << 2);
    int MMAL_BUFFER_HEADER_FLAG_FRAME                 =  (MMAL_BUFFER_HEADER_FLAG_FRAME_START|MMAL_BUFFER_HEADER_FLAG_FRAME_END);
    int MMAL_BUFFER_HEADER_FLAG_KEYFRAME               = (1 << 3);
    int MMAL_BUFFER_HEADER_FLAG_DISCONTINUITY          = (1 << 4);
    int MMAL_BUFFER_HEADER_FLAG_CONFIG                 = (1 << 5);
    int MMAL_BUFFER_HEADER_FLAG_ENCRYPTED              = (1 << 6);
    int MMAL_BUFFER_HEADER_FLAG_CODECSIDEINFO          = (1 << 7);
    int MMAL_BUFFER_HEADER_FLAGS_SNAPSHOT              = (1 << 8);
    int MMAL_BUFFER_HEADER_FLAG_CORRUPTED              = (1 << 9);
    int MMAL_BUFFER_HEADER_FLAG_TRANSMISSION_FAILED    = (1 << 10);
    int MMAL_BUFFER_HEADER_FLAG_DECODEONLY             = (1 << 11);

    int MMAL_BUFFER_HEADER_FLAG_USER0                  = (1 << 28);
    int MMAL_BUFFER_HEADER_FLAG_USER1                  = (1 << 29);
    int MMAL_BUFFER_HEADER_FLAG_USER2                  = (1 << 30);
    int MMAL_BUFFER_HEADER_FLAG_USER3                  = (1 << 31);
}


