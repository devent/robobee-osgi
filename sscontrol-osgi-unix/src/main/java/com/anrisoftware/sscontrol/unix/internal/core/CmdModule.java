package com.anrisoftware.sscontrol.unix.internal.core;

import com.anrisoftware.sscontrol.unix.internal.core.ArgumentParser.ArgumentParserFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class CmdModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(ArgumentParser.class, ArgumentParser.class)
                .build(ArgumentParserFactory.class));
    }

}
