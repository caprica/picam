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

public class MMAL_PORT_T extends Structure {

	public static class ByReference extends MMAL_PORT_T implements Structure.ByReference {
	};

	public static class ByValue extends MMAL_PORT_T implements Structure.ByValue {
	};

    private static final List<String> FIELD_ORDER = Collections.unmodifiableList(Arrays.asList(
        "priv",
        "name",
        "type",
        "index",
        "index_all",
        "is_enabled",
        "format",
        "buffer_num_min",
        "buffer_size_min",
        "buffer_alignment_min",
        "buffer_num_recommended",
        "buffer_size_recommended",
        "buffer_num",
        "buffer_size",
        "component",
        "userdata",
        "capabilities"
    ));

	public Pointer priv;

	public String name;

	public int type;

	public short index;

	public short index_all;

	public int is_enabled;

	public MMAL_ES_FORMAT_T.ByReference format;

	public int buffer_num_min;

	public int buffer_size_min;

	public int buffer_alignment_min;

	public int buffer_num_recommended;

	public int buffer_size_recommended;

	public int buffer_num;

	public int buffer_size;

	public MMAL_COMPONENT_T.ByReference component;

	public Pointer userdata;

	public int capabilities;

	public MMAL_PORT_T() {
		super();
	}

	public MMAL_PORT_T(Pointer peer) {
		super(peer);
	}

    @Override
    protected List<String> getFieldOrder() {
        return FIELD_ORDER;
    }

	public boolean isEnabled() {
		return 0 != (Integer) readField("is_enabled");
	}
}
