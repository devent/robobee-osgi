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
package com.anrisoftware.sscontrol.shell.internal.copy;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.util.Providers.of;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.copy.external.Copy;
import com.anrisoftware.sscontrol.copy.external.Copy.CopyFactory;
import com.anrisoftware.sscontrol.copy.external.CopyService;
import com.anrisoftware.sscontrol.shell.external.OpenSshShellService;
import com.anrisoftware.sscontrol.shell.external.Shell.ShellFactory;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.google.inject.AbstractModule;

/**
 * Copy command service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(CopyService.class)
public class CopyServiceImpl implements CopyService {

    @Reference
    private OpenSshShellService shellService;

    @Inject
    private CopyFactory copyFactory;

    @Override
    public Copy create(Map<String, Object> args, SshHost ssh, Object parent,
            Threads threads, Object log) {
        return copyFactory.create(args, ssh, parent, threads, log);
    }

    @Activate
    protected void start() {
        createInjector(new CopyModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(ShellFactory.class).toProvider(of(shellService));
            }
        }).injectMembers(this);
    }
}
