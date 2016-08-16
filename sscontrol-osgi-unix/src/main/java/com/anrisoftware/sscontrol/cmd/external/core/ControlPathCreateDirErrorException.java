package com.anrisoftware.sscontrol.cmd.external.core;

import java.io.File;

import org.apache.commons.lang3.exception.ContextedRuntimeException;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ControlPathCreateDirErrorException
        extends ContextedRuntimeException {

    public ControlPathCreateDirErrorException(File dir) {
        super("Error create control path directory");
        addContextValue("directory", dir);
    }

}
