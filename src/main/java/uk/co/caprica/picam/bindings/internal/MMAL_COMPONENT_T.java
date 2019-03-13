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
import com.sun.jna.ptr.PointerByReference;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MMAL_COMPONENT_T extends Structure {

	public static class ByReference extends MMAL_COMPONENT_T implements Structure.ByReference {
	};

	public static class ByValue extends MMAL_COMPONENT_T implements Structure.ByValue {
	};

	private static final List<String> FIELD_ORDER = Collections.unmodifiableList(Arrays.asList(
        "priv",
        "userdata",
        "name",
        "is_enabled",
        "control",
        "input_num",
        "input",
        "output_num",
        "output",
        "clock_num",
        "clock",
        "port_num",
        "port",
        "id"
	));

	public PointerByReference priv;

	public Pointer userdata;

	public String name;

	public int is_enabled;

	public MMAL_PORT_T.ByReference control;

	public int input_num;

	public Pointer input;

	public int output_num;

	public Pointer output;

	public int clock_num;

	public Pointer clock;

	public int port_num;

	public Pointer port;

	public int id;

	public MMAL_COMPONENT_T() {
		super();
	}

	public MMAL_COMPONENT_T(Pointer peer) {
		super(peer);
	}

	@Override
	protected List<String> getFieldOrder() {
		return FIELD_ORDER;
	}
}
