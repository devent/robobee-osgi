package com.anrisoftware.sscontrol.unix.internal.core;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;

@SuppressWarnings("serial")
public class CommandArgumentTypeNotFound extends CommandExecException {

    public CommandArgumentTypeNotFound(ClassNotFoundException e,
            String command) {
        super("Command type not found", e);
        addContextValue("command", command);
    }

}
