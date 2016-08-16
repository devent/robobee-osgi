package com.anrisoftware.sscontrol.cmd.internal.core;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;

@SuppressWarnings("serial")
public class CommandArgumentMandatoryException extends CommandExecException {

    public CommandArgumentMandatoryException(String command, String arg,
            String descr) {
        super("Mandatory argument not set");
        addContextValue("command", command);
        addContextValue("argument", arg);
        addContextValue("description", descr);
    }

}
