package com.anrisoftware.sscontrol.cmd.external.shell;

import java.util.Map;

import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.google.inject.assistedinject.Assisted;

/**
 * Factory to create the shell command.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface ShellFactory {

    Shell create(@Assisted Map<String, Object> args,
            @Assisted("parent") Object parent, @Assisted Threads threads,
            @Assisted("log") Object log, @Assisted String command);
}
