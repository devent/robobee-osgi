package com.anrisoftware.sscontrol.unix.internal.core;

import com.anrisoftware.sscontrol.unix.internal.core.CmdRun.CmdRunFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class CmdModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(CmdRun.class, CmdRun.class)
                .build(CmdRunFactory.class));
    }

}
