package uk.co.caprica.picam.bindings.internal;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public abstract class ParameterStructure extends Structure {

    public MMAL_PARAMETER_HEADER_T hdr;

    protected ParameterStructure(int id) {
        super();
        hdr.id = id;
        hdr.size = size();
    }

    protected ParameterStructure(Pointer peer) {
        super(peer);
    }
}
