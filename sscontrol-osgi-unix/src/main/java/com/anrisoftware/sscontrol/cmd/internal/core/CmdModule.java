package com.anrisoftware.sscontrol.cmd.internal.core;

import com.anrisoftware.sscontrol.cmd.internal.core.ArgsMap.ArgsMapFactory;
import com.anrisoftware.sscontrol.cmd.internal.core.CmdArgs.CmdArgsFactory;
import com.anrisoftware.sscontrol.cmd.internal.core.CmdRun.CmdRunFactory;
import com.anrisoftware.sscontrol.cmd.internal.core.SshMaster.SshMasterFactory;
import com.anrisoftware.sscontrol.cmd.internal.core.SshOptions.SshOptionsFactory;
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
        install(new FactoryModuleBuilder()
                .implement(SshMaster.class, SshMaster.class)
                .build(SshMasterFactory.class));
        install(new FactoryModuleBuilder()
                .implement(CmdArgs.class, CmdArgs.class)
                .build(CmdArgsFactory.class));
        install(new FactoryModuleBuilder()
                .implement(SshOptions.class, SshOptions.class)
                .build(SshOptionsFactory.class));
        install(new FactoryModuleBuilder()
                .implement(ArgsMap.class, ArgsMap.class)
                .build(ArgsMapFactory.class));
    }

}
