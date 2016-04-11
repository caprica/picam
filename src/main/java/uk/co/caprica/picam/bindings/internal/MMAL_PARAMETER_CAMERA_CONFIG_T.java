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

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static uk.co.caprica.picam.bindings.MmalParameters.MMAL_PARAMETER_CAMERA_CONFIG;

public class MMAL_PARAMETER_CAMERA_CONFIG_T extends ParameterStructure {

    public static class ByReference extends MMAL_PARAMETER_CAMERA_CONFIG_T implements Structure.ByReference {
    };

    public static class ByValue extends MMAL_PARAMETER_CAMERA_CONFIG_T implements Structure.ByValue {
    };

    private static final List<String> FIELD_ORDER = Collections.unmodifiableList(Arrays.asList(
        "hdr",
        "max_stills_w",
        "max_stills_h",
        "stills_yuv422",
        "one_shot_stills",
        "max_preview_video_w",
        "max_preview_video_h",
        "num_preview_video_frames",
        "stills_capture_circular_buffer_height",
        "fast_preview_resume",
        "use_stc_timestamp"
    ));

	public int max_stills_w;
	public int max_stills_h;

	public int stills_yuv422;

	public int one_shot_stills;

	public int max_preview_video_w;
	public int max_preview_video_h;
	public int num_preview_video_frames;

	public int stills_capture_circular_buffer_height;
	public int fast_preview_resume;
	public int use_stc_timestamp;

	public MMAL_PARAMETER_CAMERA_CONFIG_T() {
		super(MMAL_PARAMETER_CAMERA_CONFIG);
	}

    public MMAL_PARAMETER_CAMERA_CONFIG_T(Pointer peer) {
        super(peer);
    }

    @Override
	protected List<String> getFieldOrder() {
		return FIELD_ORDER;
	}
}
