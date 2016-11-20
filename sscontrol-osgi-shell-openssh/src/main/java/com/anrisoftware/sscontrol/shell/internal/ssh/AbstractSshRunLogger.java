package com.anrisoftware.sscontrol.shell.internal.ssh;

import static com.anrisoftware.sscontrol.shell.internal.ssh.AbstractSshRunLogger._.command_finished_debug;
import static com.anrisoftware.sscontrol.shell.internal.ssh.AbstractSshRunLogger._.command_finished_info;
import static com.anrisoftware.sscontrol.shell.internal.ssh.AbstractSshRunLogger._.command_finished_trace;

import java.util.Map;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link AbstractSshRun}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class AbstractSshRunLogger extends AbstractLogger {

    enum _ {

        command_finished_trace("Command finished: {} for {}, {}."),

        command_finished_debug("Command finished: for {}."),

        command_finished_info("Command finished for '{}'.");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link AbstractSshRun}.
     */
    public AbstractSshRunLogger() {
        super(AbstractSshRun.class);
    }

    void commandFinished(Object parent, ProcessTask task,
            Map<String, Object> args) {
        if (isTraceEnabled()) {
            trace(command_finished_trace, args, parent, task);
        } else if (isDebugEnabled()) {
            debug(command_finished_debug, args, parent);
        } else {
            info(command_finished_info, parent);
        }
    }

}
