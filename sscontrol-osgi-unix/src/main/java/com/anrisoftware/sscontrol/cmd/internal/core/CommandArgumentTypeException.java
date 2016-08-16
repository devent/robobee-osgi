package com.anrisoftware.sscontrol.cmd.internal.core;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;

@SuppressWarnings("serial")
public class CommandArgumentTypeException extends CommandExecException {

    public CommandArgumentTypeException(String command, String arg,
            Class<?> type, String descr) {
        super("Argument type not match");
        addContextValue("command", command);
        addContextValue("argument", arg);
        addContextValue("type", type);
        addContextValue("description", descr);
    }

}
