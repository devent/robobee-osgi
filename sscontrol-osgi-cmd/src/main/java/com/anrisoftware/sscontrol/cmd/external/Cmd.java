package com.anrisoftware.sscontrol.cmd.external;

import java.util.Map;

import org.joda.time.Duration;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.exec.internal.runcommands.RunCommands;
import com.anrisoftware.globalpom.exec.internal.scriptprocess.AbstractProcessExec;
import com.anrisoftware.sscontrol.types.external.SscontrolServiceScript;

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
     *            the {@link SscontrolServiceScript} parent script.
     *
     * @return the {@link ProcessTask}.
     *
     */
    ProcessTask call(Map<String, Object> args, String command,
            SscontrolServiceScript parent) throws CommandExecException;
}
