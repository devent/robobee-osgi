package com.anrisoftware.sscontrol.shell.external.ssh;

import static com.anrisoftware.sscontrol.shell.external.ssh.AbstractSshRunLogger._.setup_remote_finished_debug;
import static com.anrisoftware.sscontrol.shell.external.ssh.AbstractSshRunLogger._.setup_remote_finished_info;
import static com.anrisoftware.sscontrol.shell.external.ssh.AbstractSshRunLogger._.setup_remote_finished_trace;

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

        setup_remote_finished_trace("Setup remote finished: {} for {}, {}."),

        setup_remote_finished_debug("Setup remote finished: for {}."),

        setup_remote_finished_info("Setup remote finished for '{}'.");

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

    void setupRemoteFinished(Object parent, ProcessTask task,
            Map<String, Object> args) {
        if (isTraceEnabled()) {
            trace(setup_remote_finished_trace, args, parent, task);
        } else if (isDebugEnabled()) {
            debug(setup_remote_finished_debug, args, parent);
        } else {
            info(setup_remote_finished_info, parent);
        }
    }

}
