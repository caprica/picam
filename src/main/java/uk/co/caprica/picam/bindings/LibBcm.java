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

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.ptr.PointerByReference;
import uk.co.caprica.picam.bindings.internal.MMAL_BUFFER_HEADER_T;
import uk.co.caprica.picam.bindings.internal.MMAL_COMPONENT_T;
import uk.co.caprica.picam.bindings.internal.MMAL_ES_FORMAT_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PARAMETER_HEADER_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PORT_BH_CB_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PORT_T;

// FIXME is this actually needed?

public interface LibBcm extends Library {

    String LIBRARY_NAME = "bcm_host";

    LibBcm bcm = (LibBcm) Native.loadLibrary(LIBRARY_NAME, LibBcm.class);

    NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance(LIBRARY_NAME);

    void bcm_host_init();
}
