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
package com.anrisoftware.sscontrol.cmd.internal.shell;

import static com.google.inject.util.Providers.of;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.cmd.external.Cmd;
import com.anrisoftware.sscontrol.cmd.external.Shell;
import com.anrisoftware.sscontrol.cmd.external.Shell.ShellFactory;
import com.anrisoftware.sscontrol.cmd.external.shell.ShellService;
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
@Service(ShellService.class)
public class ShellServiceImpl implements ShellService {

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
