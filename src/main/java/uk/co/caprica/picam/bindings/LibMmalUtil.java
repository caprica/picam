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

package uk.co.caprica.picam.bindings;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import uk.co.caprica.picam.bindings.internal.MMAL_POOL_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PORT_T;

public interface LibMmalUtil extends Library {

	String JNA_LIBRARY_NAME = "mmal_util";

	LibMmalUtil mmalUtil = Native.load(LibMmalUtil.JNA_LIBRARY_NAME, LibMmalUtil.class);

	MMAL_POOL_T mmal_port_pool_create(MMAL_PORT_T port, int headers, int payload_size);

	void mmal_port_pool_destroy(MMAL_PORT_T port, MMAL_POOL_T pool);

	int mmal_connection_create(PointerByReference connection, MMAL_PORT_T out, MMAL_PORT_T in, int flags);

	int mmal_connection_enable(Pointer connection);

	int mmal_connection_disable(Pointer connection);

	int mmal_connection_destroy(Pointer connection);

}
