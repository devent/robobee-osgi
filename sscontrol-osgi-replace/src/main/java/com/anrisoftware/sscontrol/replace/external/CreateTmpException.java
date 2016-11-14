package com.anrisoftware.sscontrol.replace.external;

import java.io.IOException;

import com.anrisoftware.sscontrol.types.external.AppException;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class CreateTmpException extends AppException {

    public CreateTmpException(IOException e) {
        super("Error create tmp file", e);
    }

}
