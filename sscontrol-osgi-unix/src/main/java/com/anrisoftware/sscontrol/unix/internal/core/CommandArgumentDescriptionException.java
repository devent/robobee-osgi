package com.anrisoftware.sscontrol.unix.internal.core;

import java.io.IOException;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;

@SuppressWarnings("serial")
public class CommandArgumentDescriptionException extends CommandExecException {

    public CommandArgumentDescriptionException(String command) {
        super("Unparseable description for argument");
        addContextValue("command", command);
    }

    public CommandArgumentDescriptionException(IOException e, String command) {
        super("Unparseable description for argument", e);
        addContextValue("command", command);
    }

}
