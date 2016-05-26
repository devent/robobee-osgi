package com.anrisoftware.sscontrol.unix.external.core;

import java.util.Map;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.exec.internal.runcommands.RunCommands;
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
     *            <li>{@code runCommands} optionally, set to the
     *            {@link RunCommands} to record the command.
     *
     *            <li>{@code command} the change file owner command, for example
     *            {@code "/bin/chmod".}
     *
     *            <li>etc.
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
