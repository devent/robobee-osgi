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
