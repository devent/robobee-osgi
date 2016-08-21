package com.anrisoftware.sscontrol.cmd.internal.shell;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.cmd.external.Cmd;
import com.anrisoftware.sscontrol.cmd.external.shell.Shell;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ShellImpl implements Shell {

    private final Threads threads;

    private final Object parent;

    private final Map<String, Object> args;

    private final String command;

    private final Object log;

    @Inject
    private Cmd cmd;

    @Inject
    ShellImpl(@Assisted Map<String, Object> args,
            @Assisted("parent") Object parent, @Assisted Threads threads,
            @Assisted("log") Object log, @Assisted String command) {
        this.args = args;
        this.parent = parent;
        this.threads = threads;
        this.log = log;
        this.command = command;
    }

    @Override
    public ProcessTask call() throws CommandExecException {
        args.put("log", log);
        return cmd.call(args, parent, threads, command);
    }
}
