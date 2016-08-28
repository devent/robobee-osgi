package com.anrisoftware.sscontrol.services.internal;

import com.anrisoftware.sscontrol.services.internal.TargetsImpl.TargetsImplFactory;
import com.anrisoftware.sscontrol.types.external.Targets;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class TargetsModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(Targets.class, TargetsImpl.class)
                .build(TargetsImplFactory.class));
    }

}
