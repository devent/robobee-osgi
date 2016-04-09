/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.debug.internal;

import static com.anrisoftware.sscontrol.debug.internal.DebugLoggingImplLogger._.modulePut;
import static com.anrisoftware.sscontrol.debug.internal.DebugLoggingImplLogger._.moduleRemoved;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.debug.external.DebugModule;

/**
 * Logging for {@link DebugLoggingImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class DebugLoggingImplLogger extends AbstractLogger {

    enum _ {

        modulePut("Module {} put to {}"),

        moduleRemoved("Module '{}' removed from {}");

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
     * Sets the context of the logger to {@link DebugLoggingImpl}.
     */
    public DebugLoggingImplLogger() {
        super(DebugLoggingImpl.class);
    }

    void modulePut(DebugLoggingImpl debug, DebugModule module) {
        debug(modulePut, module, debug);
    }

    void moduleRemoved(DebugLoggingImpl debug, String name) {
        debug(moduleRemoved, name, debug);
    }

}
