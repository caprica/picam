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

package uk.co.caprica.picam.bindings.internal;

public interface MMAL_PARAMETER_CAMERA_CONFIG_TIMESTAMP_MODE_T {

    int MMAL_PARAM_TIMESTAMP_MODE_ZERO = 0;
    int MMAL_PARAM_TIMESTAMP_MODE_RAW_STC = 1;
    int MMAL_PARAM_TIMESTAMP_MODE_RESET_STC = 2;
    int MMAL_PARAM_TIMESTAMP_MODE_MAX = 0x7FFFFFFF;
}
