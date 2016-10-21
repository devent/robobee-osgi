/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-unix.
 *
 * sscontrol-osgi-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-unix. If not, see <http://www.gnu.org/licenses/>.
 */
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
