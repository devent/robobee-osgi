package com.anrisoftware.sscontrol.cmd.external.core;

import java.text.ParseException;

import org.apache.commons.lang3.exception.ContextedRuntimeException;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ParsePropertiesErrorException extends ContextedRuntimeException {

    public ParsePropertiesErrorException(ParseException e, String property) {
        super("Error parse profile property", e);
        addContextValue("property", property);
    }

}
