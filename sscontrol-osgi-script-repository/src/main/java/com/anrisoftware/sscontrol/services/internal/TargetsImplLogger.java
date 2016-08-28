package com.anrisoftware.sscontrol.services.internal;

import static com.anrisoftware.sscontrol.services.internal.TargetsImplLogger._.addHosts;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.types.external.Ssh;

/**
 * Logging for {@link TargetsImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class TargetsImplLogger extends AbstractLogger {

    enum _ {

        addHosts("Add hosts {} to {}");

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
     * Sets the context of the logger to {@link TargetsImpl}.
     */
    public TargetsImplLogger() {
        super(TargetsImpl.class);
    }

    public void addHosts(TargetsImpl targets, Ssh ssh) {
        debug(addHosts, ssh, targets);
    }
}
