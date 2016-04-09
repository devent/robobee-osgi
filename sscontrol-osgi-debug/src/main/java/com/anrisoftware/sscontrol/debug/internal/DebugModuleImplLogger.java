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

import static com.anrisoftware.sscontrol.debug.internal.DebugModuleImplLogger._.debugSet;
import static com.anrisoftware.sscontrol.debug.internal.DebugModuleImplLogger._.propertyPut;

import java.util.Map;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link DebugModuleImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class DebugModuleImplLogger extends AbstractLogger {

    enum _ {

        debugSet("Module {} set for {}."),

        propertyPut("Property '{}'={} put for {}.");

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
     * Sets the context of the logger to {@link DebugModuleImpl}.
     */
    public DebugModuleImplLogger() {
        super(DebugModuleImpl.class);
    }

    void debugSet(DebugModuleImpl module, Map<String, Object> args) {
        debug(debugSet, args, module);
    }

    void propertyPut(DebugModuleImpl module, String property, Object value) {
        debug(propertyPut, property, value, module);
    }
}
