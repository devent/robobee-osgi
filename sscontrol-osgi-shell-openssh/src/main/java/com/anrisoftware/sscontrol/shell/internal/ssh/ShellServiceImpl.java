/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-shell-openssh.
 *
 * sscontrol-osgi-shell-openssh is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-shell-openssh is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-shell-openssh. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.shell.internal.ssh;

import static com.google.inject.util.Providers.of;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.shell.external.Cmd;
import com.anrisoftware.sscontrol.shell.external.Shell;
import com.anrisoftware.sscontrol.shell.external.Shell.ShellFactory;
import com.anrisoftware.sscontrol.shell.external.ssh.OpenSshShellService;
import com.anrisoftware.sscontrol.shell.external.ShellService;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.assistedinject.Assisted;

/**
 * Creates the shell.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service({ ShellService.class, OpenSshShellService.class })
public class ShellServiceImpl implements ShellService, OpenSshShellService {

    @Inject
    private ShellFactory shellFactory;

    @Reference
    private Cmd cmd;

    @Override
    public Shell create(Map<String, Object> args, @Assisted SshHost ssh,
            Object parent, Threads threads, Object log, String command) {
        return shellFactory.create(args, ssh, parent, threads, log, command);
    }

    @Activate
    protected void start() {
        Guice.createInjector(new ShellModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(Cmd.class).toProvider(of(cmd));
            }
        }).injectMembers(this);
    }
}
