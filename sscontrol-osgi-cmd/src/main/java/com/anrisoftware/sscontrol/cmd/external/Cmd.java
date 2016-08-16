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
     *            <li>{@code shell} optionally, sets the shell that is used to
     *            execute the command on the host. The shell must be installed
     *            on the host system. For example, {@code bash -s}.
     * 
     *            <li>{@code chdir} optionally, sets the work directory of the
     *            command. If not set, the working directory is the user home
     *            directory of the ssh user.
     * 
     *            <li>{@code env} optionally, sets the {@link Map} of
     *            environment variables to set for the command.
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
     *            <li>{@code sshPort} optionally, sets the {@link Integer} port
     *            number of the ssh server.
     * 
     *            <li>{@code sshArgs} optionally, sets the ssh arguments and
     *            options.
     *
     *            <li>{@code sshControlMaster} optionally, sets if to use a ssh
     *            control master. Must be a valid ssh option.
     *
     *            <li>{@code sshControlPersistDuration} optionally, set the
     *            {@link Duration} how long the master persists.
     *
     *            <li>{@code sshControlPath} optionally, set the {@link String}
     *            of the control master socket path.
     *
     *            <li>{@code sshConnectionTimeout} optionally, sets the
     *            {@link Duration} for the ssh connection timeout.
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
     * @param parent
     *            the parent script.
     *
     * @param threads
     *            the {@link Threads} threads pool to execute the command on.
     *
     * @param command
     *            the {@link String} command.
     *
     * @return the {@link ProcessTask}.
     *
     */
    ProcessTask call(Map<String, Object> args, Object parent, Threads threads,
            String command) throws CommandExecException;
}
