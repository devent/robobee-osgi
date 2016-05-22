package com.anrisoftware.sscontrol.unix.external.core;

import java.util.Map;
import java.util.Properties;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.exec.internal.runcommands.RunCommands;
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
     *            the {@link Object} parent script.
     *
     * @param threads
     *            the {@link Threads} pool.
     * 
     * @param properties
     *            the {@link Properties} of the environment.
     * 
     * @return the {@link ProcessTask}.
     *
     */
    ProcessTask call(Map<String, Object> args, String command, Object parent,
            Threads threads, Properties properties) throws CommandExecException;
}
