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
package com.anrisoftware.sscontrol.types.groovy.internal;

import static com.anrisoftware.sscontrol.types.groovy.internal.BindingHostImplLogger._.hostAddAdded;
import static com.anrisoftware.sscontrol.types.groovy.internal.BindingHostImplLogger._.hostRemoveAdded;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.types.external.BindingHost.Host;

/**
 * Logging for {@link BindingHostImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class BindingHostImplLogger extends AbstractLogger {

    enum _ {

        hostAddAdded("Host to add {} added to {}"),

        hostRemoveAdded("Host to remove {} added to {}");

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
     * Sets the context of the logger to {@link BindingHostImpl}.
     */
    public BindingHostImplLogger() {
        super(BindingHostImpl.class);
    }

    void hostAddAdded(BindingHostImpl binding, Host host) {
        debug(hostAddAdded, host, binding);
    }

    void hostRemoveAdded(BindingHostImpl binding, Host host) {
        debug(hostRemoveAdded, host, binding);
    }
}
