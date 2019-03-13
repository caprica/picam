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

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;

public class MMAL_ES_SPECIFIC_FORMAT_T extends Union {

    public static class ByReference extends MMAL_ES_SPECIFIC_FORMAT_T implements Structure.ByReference {
    };

    public static class ByValue extends MMAL_ES_SPECIFIC_FORMAT_T implements Structure.ByValue {
    };

	public MMAL_AUDIO_FORMAT_T audio;

	public MMAL_VIDEO_FORMAT_T video;

	public MMAL_SUBPICTURE_FORMAT_T subpicture;

	public MMAL_ES_SPECIFIC_FORMAT_T() {
		super();
	}

	public MMAL_ES_SPECIFIC_FORMAT_T(Pointer peer) {
		super(peer);
	}
}
