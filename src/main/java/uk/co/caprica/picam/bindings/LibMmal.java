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
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import uk.co.caprica.picam.bindings.internal.MMAL_BUFFER_HEADER_T;
import uk.co.caprica.picam.bindings.internal.MMAL_COMPONENT_T;
import uk.co.caprica.picam.bindings.internal.MMAL_ES_FORMAT_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PORT_BH_CB_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PORT_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PARAMETER_HEADER_T;

public interface LibMmal extends Library {

    String LIBRARY_NAME = "mmal";

    LibMmal mmal = (LibMmal) Native.loadLibrary(LIBRARY_NAME, LibMmal.class);

    NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance(LIBRARY_NAME);

    int mmal_component_create(String name, PointerByReference component);

    int mmal_component_destroy(MMAL_COMPONENT_T component);

    int mmal_component_enable(MMAL_COMPONENT_T component);

    int mmal_component_disable(MMAL_COMPONENT_T component);

    int mmal_port_parameter_set(MMAL_PORT_T port, MMAL_PARAMETER_HEADER_T param);

    void mmal_format_copy(MMAL_ES_FORMAT_T format_dest, MMAL_ES_FORMAT_T format_src);

    int mmal_format_full_copy(MMAL_ES_FORMAT_T format_dest, MMAL_ES_FORMAT_T format_src);

    int mmal_port_format_commit(MMAL_PORT_T port);

    int mmal_port_enable(MMAL_PORT_T port, MMAL_PORT_BH_CB_T cb);

    int mmal_port_disable(MMAL_PORT_T port);

    int mmal_queue_length(PointerByReference queue);

    MMAL_BUFFER_HEADER_T mmal_queue_get(PointerByReference queue);

    int mmal_port_send_buffer(MMAL_PORT_T port, MMAL_BUFFER_HEADER_T buffer);

    int mmal_buffer_header_mem_lock(MMAL_BUFFER_HEADER_T header);

    void mmal_buffer_header_mem_unlock(MMAL_BUFFER_HEADER_T header);

    void mmal_buffer_header_release(MMAL_BUFFER_HEADER_T header);
}
