package com.anrisoftware.sscontrol.shell.external;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.sscontrol.types.external.AppException;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ShellExecException extends AppException {

    public ShellExecException(CommandExecException e, String command) {
        super("Executing error", e);
        addContextValue("command", command);
    }

}
