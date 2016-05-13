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

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.picam.bindings.internal.MMAL_COMPONENT_T;
import uk.co.caprica.picam.bindings.internal.MMAL_PORT_T;

import static uk.co.caprica.picam.bindings.LibMmal.mmal;
import static uk.co.caprica.picam.bindings.LibMmalUtil.mmalUtil;
import static uk.co.caprica.picam.bindings.MmalConnectionFlag.MMAL_CONNECTION_FLAG_ALLOCATION_ON_INPUT;
import static uk.co.caprica.picam.bindings.MmalConnectionFlag.MMAL_CONNECTION_FLAG_TUNNELLING;
import static uk.co.caprica.picam.bindings.internal.MMAL_STATUS_T.MMAL_SUCCESS;

final class MmalUtils {

    private static final Logger logger = LoggerFactory.getLogger(MmalUtils.class);

    private MmalUtils() {
    }

    static MMAL_COMPONENT_T createComponent(String name) {
        logger.debug("createComponent(name={})", name);

        PointerByReference componentRef = new PointerByReference();
        int result = mmal.mmal_component_create(name, componentRef);
        logger.debug("result={}", result);

        if (result != MMAL_SUCCESS) {
            throw new RuntimeException("Failed to create component");
        }

        MMAL_COMPONENT_T component = new MMAL_COMPONENT_T(componentRef.getValue());
        component.read();
        logger.trace("component={}", component);

        logger.debug("component.name={}", component.name);

        return component;
    }

    static void enableComponent(MMAL_COMPONENT_T component) {
        logger.debug("enableComponent()");

        logger.debug("component.name={}", component.name);

        int result = mmal.mmal_component_enable(component);
        logger.debug("result={}", result);

        if (result != MMAL_SUCCESS) {
            throw new RuntimeException("Failed to enable component");
        }
    }

    static void disableComponent(MMAL_COMPONENT_T component) {
        logger.debug("disableComponent()");

        if (component != null) {
            logger.debug("component.name={}", component.name);
            int result = mmal.mmal_component_disable(component);
            logger.debug("result={}", result);
        }
    }

    static void destroyComponent(MMAL_COMPONENT_T component) {
        logger.debug("destroyComponent()");

        if (component != null) {
            logger.debug("component.name={}", component.name);
            int result = mmal.mmal_component_destroy(component);
            logger.debug("result={}", result);
        }
    }

    static MMAL_PORT_T getPort(Pointer pointer) {
        logger.debug("getPort()");
        MMAL_PORT_T port = new MMAL_PORT_T(pointer);
        port.read();
        return port;
    }

    static int connectPorts(MMAL_PORT_T output_port, MMAL_PORT_T input_port, PointerByReference connection) {
        logger.debug("connectPorts()");

        int result =  mmalUtil.mmal_connection_create(connection, output_port, input_port, MMAL_CONNECTION_FLAG_TUNNELLING | MMAL_CONNECTION_FLAG_ALLOCATION_ON_INPUT);
        logger.debug("result={}", result);

        logger.trace("connection={}", connection);

        if (result == MMAL_SUCCESS) {
            result =  mmalUtil.mmal_connection_enable(connection.getValue());
            logger.debug("result={}", result);

            if (result != MMAL_SUCCESS) {
                mmalUtil.mmal_connection_destroy(connection.getValue()); // FIXME is this really necessary, normal clean-up should destroy the connection?
            }
        }
        return result;
    }

    static int disablePort(MMAL_PORT_T port) {
        logger.debug("disablePort()");

        int result = MMAL_SUCCESS;
        if (port != null) {
            logger.debug("port.name={}", port.name);
            boolean enabled = port.isEnabled();
            logger.debug("enabled={}", enabled);
            if (enabled) {
                result = mmal.mmal_port_disable(port);
            }
        }
        logger.debug("result={}", result);

        return result;
    }

    static void destroyConnection(Pointer connection) {
        logger.debug("destroyConnection()");

        if (connection != null) {
            int result = mmalUtil.mmal_connection_destroy(connection);
            logger.debug("result={}", result);
        }
    }
}
