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
package com.anrisoftware.sscontrol.services.internal;

import static com.anrisoftware.sscontrol.services.internal.HostServicesImplLogger._.addService;
import static com.anrisoftware.sscontrol.services.internal.HostServicesImplLogger._.availableServiceAdded;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.types.external.HostService;
import com.anrisoftware.sscontrol.types.external.HostServiceService;

/**
 * Logging for {@link HostServicesImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class HostServicesImplLogger extends AbstractLogger {

    enum _ {

        addService("Service '{}':{} added to {}"),

        availableServiceAdded("Available service '{}':{} added to {}");

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
     * Sets the context of the logger to {@link HostServicesImpl}.
     */
    public HostServicesImplLogger() {
        super(HostServicesImpl.class);
    }

    void addService(HostServicesImpl services, String name,
            HostService service) {
        debug(addService, name, service, services);
    }

    void availableServiceAdded(HostServicesImpl services, String name,
            HostServiceService service) {
        debug(availableServiceAdded, name, service, services);
    }
}
