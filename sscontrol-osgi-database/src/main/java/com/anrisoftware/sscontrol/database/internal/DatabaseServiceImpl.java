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
package com.anrisoftware.sscontrol.database.internal;

import static com.google.inject.util.Providers.of;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.globalpom.strings.ToStringService;
import com.anrisoftware.sscontrol.database.external.Database;
import com.anrisoftware.sscontrol.database.external.DatabaseService;
import com.anrisoftware.sscontrol.database.internal.DatabaseImpl.DatabaseImplFactory;
import com.anrisoftware.sscontrol.debug.external.DebugService;
import com.anrisoftware.sscontrol.types.external.BindingHostService;
import com.anrisoftware.sscontrol.types.external.UserPasswordService;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

/**
 * Creates the database.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(DatabaseService.class)
public class DatabaseServiceImpl implements DatabaseService {

    @Inject
    private DatabaseImplFactory databaseFactory;

    @Reference
    private UserPasswordService userPasswordService;

    @Reference
    private ToStringService toStringService;

    @Reference
    private DebugService debugService;

    @Reference
    private BindingHostService bindingHostService;

    @Override
    public Database create(Map<String, Object> args) {
        return (Database) databaseFactory.create(args);
    }

    @Activate
    protected void start() {
        Guice.createInjector(new DatabaseModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(UserPasswordService.class)
                        .toProvider(of(userPasswordService));
                bind(ToStringService.class).toProvider(of(toStringService));
                bind(DebugService.class).toProvider(of(debugService));
                bind(BindingHostService.class)
                        .toProvider(of(bindingHostService));
            }
        }).injectMembers(this);
    }
}
