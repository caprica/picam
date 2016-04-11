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

package uk.co.caprica.picam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.picam.bindings.internal.MMAL_PARAMETER_BOOLEAN_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PARAMETER_INT32_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PARAMETER_RATIONAL_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PARAMETER_UINT32_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PORT_T;
import uk.co.caprica.picam.bindings.internal.MMAL_RATIONAL_T;
import uk.co.caprica.picam.bindings.internal.ParameterStructure;

import static uk.co.caprica.picam.bindings.LibMmal.mmal;

/**
 *
 */
final class MmalParameterUtils {

    private static final Logger logger = LoggerFactory.getLogger(MmalParameterUtils.class);

    private MmalParameterUtils() {
    }

    static int mmal_port_parameter_set_int32(MMAL_PORT_T port, int id, int value) {
        logger.debug("mmal_port_parameter_set_int32(id={},value={})", id, value);

        MMAL_PARAMETER_INT32_T param = new MMAL_PARAMETER_INT32_T();
        param.hdr.id = id;
        param.hdr.size = param.size();
        param.value = value;
        param.write();

        logger.trace("param={}", param);

        return mmal.mmal_port_parameter_set(port, param.hdr);
    }

    static int mmal_port_parameter_set_uint32(MMAL_PORT_T port, int id, int value) {
        logger.debug("mmal_port_parameter_set_uint32(id={},value={})", id, value);

        MMAL_PARAMETER_UINT32_T param = new MMAL_PARAMETER_UINT32_T();
        param.hdr.id = id;
        param.hdr.size = param.size();
        param.value = value;
        param.write();

        logger.trace("param={}", param);

        return mmal.mmal_port_parameter_set(port, param.hdr);
    }

    static int mmal_port_parameter_set_boolean(MMAL_PORT_T port, int id, int enable) {
        logger.debug("mmal_port_parameter_set_boolean(id={},enable={})", id, enable);

        MMAL_PARAMETER_BOOLEAN_T param = new MMAL_PARAMETER_BOOLEAN_T();
        param.hdr.id = id;
        param.hdr.size = param.size();
        param.enable = enable;
        param.write();

        logger.trace("param={}", param);

        return mmal.mmal_port_parameter_set(port, param.hdr);
    }

    static int mmal_port_parameter_set_rational(MMAL_PORT_T port, int id, MMAL_RATIONAL_T value) {
        logger.debug("mmal_port_parameter_set_rational(id={})", id);

        MMAL_PARAMETER_RATIONAL_T param = new MMAL_PARAMETER_RATIONAL_T();
        param.hdr.id = id;
        param.hdr.size = param.size();
        param.value = value;
        param.write();

        logger.trace("param={}", param);

        return mmal.mmal_port_parameter_set(port, param.hdr);
    }

    static int mmal_port_parameter_set(MMAL_PORT_T port, ParameterStructure param) {
        logger.debug("mmal_port_parameter_set(param={})", param.hdr.id);

        param.write();

        logger.trace("param={}", param);

        return mmal.mmal_port_parameter_set(port, param.hdr);
    }
}
