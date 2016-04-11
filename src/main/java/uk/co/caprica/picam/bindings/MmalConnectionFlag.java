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

package uk.co.caprica.picam.bindings;

public interface MmalConnectionFlag {

    int MMAL_CONNECTION_FLAG_TUNNELLING = 0x1;
    int MMAL_CONNECTION_FLAG_ALLOCATION_ON_INPUT = 0x2;
    int MMAL_CONNECTION_FLAG_ALLOCATION_ON_OUTPUT = 0x4;
    int MMAL_CONNECTION_FLAG_KEEP_BUFFER_REQUIREMENTS = 0x8;
    int MMAL_CONNECTION_FLAG_DIRECT = 0x10;
}
