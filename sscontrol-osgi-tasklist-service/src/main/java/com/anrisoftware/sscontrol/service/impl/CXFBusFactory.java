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
package com.anrisoftware.sscontrol.service.impl;

import javax.annotation.PreDestroy;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.blueprint.BlueprintBus;
import org.apache.cxf.bus.extension.ExtensionManagerBus;
import org.osgi.framework.BundleContext;
import org.osgi.service.blueprint.container.BlueprintContainer;

@Singleton
public class CXFBusFactory {
    private BlueprintBus bus;

    @Inject
    BundleContext bundleContext;

    @Inject
    BlueprintContainer blueprintContainer;

    @Produces
    public Bus create() {
        if (bus == null) {
            bus = new BlueprintBus();
            bus.setBundleContext(bundleContext);
            bus.setBlueprintContainer(blueprintContainer);
            bus.initialize();
        }
        return bus;
    }

    @PreDestroy
    public void destroyBus() {
        if (bus != null) {
            ((ExtensionManagerBus)bus).shutdown();
        }
    }
}
