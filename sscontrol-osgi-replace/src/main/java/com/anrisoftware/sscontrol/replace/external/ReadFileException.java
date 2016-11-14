package com.anrisoftware.sscontrol.replace.external;

import java.io.File;
import java.io.IOException;

import com.anrisoftware.sscontrol.types.external.AppException;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ReadFileException extends AppException {

    public ReadFileException(File file, IOException e) {
        super("Read file error", e);
        addContextValue("file", file);
    }

}
