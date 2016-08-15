package com.anrisoftware.sscontrol.cmd.external;

import java.util.Map;

import org.joda.time.Duration;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.exec.internal.runcommands.RunCommands;
import com.anrisoftware.globalpom.exec.internal.scriptprocess.AbstractProcessExec;
import com.anrisoftware.globalpom.threads.external.core.Threads;

/**
 * Runs the specified command.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Cmd {

    /**
     * Runs the specified command.
     *
     * @param args
     *            the {@link Map} arguments:
     *            <ul>
     *            <li>{@code log} the logger that logs the command output;
     *
     *            <li>{@code runCommands} optionally, set to the
     *            {@link RunCommands} to record the command.
     * 
     *            <li>{@code privileged} optionally, set to {@code true} to run
     *            the code with privileged rights.
     *
     *            <li>{@code shell} sets the shell that is used to execute the
     *            command on the host. The shell must be installed on the host
     *            system. For example, {@code /bin/bash}.
     * 
     *            <li>{@code sshShell} optionally, sets the shell that is used
     *            to call ssh. The shell is normally platform dependent, for
     *            example, on GNU/Linux systems the bash shell is the most used.
     *            The shell must be installed on the client system.
     * 
     *            <li>{@code sshUser} optionally, sets the ssh user to connect
     *            to the host system. The user must be available on the host and
     *            the client must be allowed to login. Normally, that means that
     *            public key must be added to the host user's
     *            {@code authorized_keys} file. Per default the current user is
     *            used.
     * 
     *            <li>{@code sshHost} sets the remote host.
     * 
     *            <li>{@code sshArgs} optionally, sets the ssh arguments and
     *            options.
     *
     *            <li>{@code outString} optionally, set to {@code true} to save
     *            the output in a {@link String} for later parsing, see
     *            {@link ProcessTask#getOut()};
     *
     *            <li>{@code errString} optionally, set to {@code true} to save
     *            the error output in a {@link String} for later parsing, see
     *            {@link ProcessTask#getErr()}. Per default it is set to
     *            {@link AbstractProcessExec#ERR_STRING_DEFAULT}.
     *
     *            <li>{@code timeout} optionally, set the timeout
     *            {@link Duration};
     *
     *            <li>{@code destroyOnTimeout} optionally, set to {@code true}
     *            to destroy the process on timeout;
     *
     *            <li>{@code checkExitCodes} optionally, set to {@code true} to
     *            check the exit code(s) of the process;
     *
     *            <li>{@code exitCodes} optionally, set an int-array of success
     *            exit codes;
     *
     *            <li>{@code exitCode} optionally, set the success exit code of
     *            the process;
     *            </ul>
     * 
     * @param command
     *            the {@link String} command.
     *
     * @param parent
     *            the parent script.
     *
     * @return the {@link ProcessTask}.
     *
     */
    ProcessTask call(Object parent, Threads threads, String command,
            Map<String, Object> args) throws CommandExecException;
}
