package com.anrisoftware.sscontrol.cmd.external;

import java.util.Map;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * Shell command.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Shell {

    /**
     * Factory to create the shell command.
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ShellFactory {

        Shell create(@Assisted Map<String, Object> args, @Assisted SshHost ssh,
                @Assisted("parent") Object parent, @Assisted Threads threads,
                @Assisted("log") Object log, @Assisted String command);
    }

    /**
     * Executes the shell.
     * 
     * @throws CommandExecException
     */
    ProcessTask call() throws CommandExecException;

}
