package com.anrisoftware.sscontrol.cmd.external.shell;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;

/**
 * Shell command.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Shell {

    /**
     * Executes the shell.
     * 
     * @throws CommandExecException
     */
    ProcessTask call() throws CommandExecException;

}
