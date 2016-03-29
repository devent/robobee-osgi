/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-app-cli.
 *
 * sscontrol-osgi-app-cli is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-app-cli is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-app-cli. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.app;

import static com.anrisoftware.sscontrol.app.AppLogger._.errorLaunchFramework;
import static com.anrisoftware.sscontrol.app.AppLogger._.errorShutdownFramework;
import static com.anrisoftware.sscontrol.app.AppLogger._.restartFramework;
import static com.anrisoftware.sscontrol.app.AppLogger._.timeoutStopFramework;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link Starter}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class AppLogger extends AbstractLogger {

    enum _ {

        errorLaunchFramework("Could not launch framework"),

        restartFramework(
                "Timeout waiting for framework to stop. Restarting now."),

        timeoutStopFramework(
                "Timeout waiting for framework to stop. Exiting VM."),

        errorShutdownFramework("Error occurred shutting down framework.");

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
     * Sets the context of the logger to {@link Starter}.
     */
    public AppLogger() {
        super(Starter.class);
    }

    void errorLaunchFramework(Throwable ex) {
        error(errorLaunchFramework, ex);
    }

    void restartFramework() {
        debug(restartFramework);
    }

    void timeoutStopFramework() {
        error(timeoutStopFramework);
    }

    void errorShutdownFramework(Throwable ex) {
        error(errorShutdownFramework, ex);
    }
}
