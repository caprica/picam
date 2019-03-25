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

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import uk.co.caprica.picam.bindings.internal.MMAL_POOL_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PORT_T;

public final class LibMmalUtil {

	static {
		Native.register("mmal_util");
	}

	public static native MMAL_POOL_T mmal_port_pool_create(MMAL_PORT_T port, int headers, int payload_size);

	public static native void mmal_port_pool_destroy(MMAL_PORT_T port, MMAL_POOL_T pool);

	public static native int mmal_connection_create(PointerByReference connection, MMAL_PORT_T out, MMAL_PORT_T in, int flags);

	public static native int mmal_connection_enable(Pointer connection);

	public static native int mmal_connection_disable(Pointer connection);

	public static native int mmal_connection_destroy(Pointer connection);

}
