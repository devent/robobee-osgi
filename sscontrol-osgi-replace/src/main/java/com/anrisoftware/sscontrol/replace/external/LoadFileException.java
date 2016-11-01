package com.anrisoftware.sscontrol.replace.external;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import com.anrisoftware.sscontrol.types.external.AppException;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class LoadFileException extends AppException {

    public LoadFileException(URL dest, Charset charset, IOException e) {
        super("Load file error", e);
        addContextValue("dest", dest);
        addContextValue("charset", charset);
    }

}
