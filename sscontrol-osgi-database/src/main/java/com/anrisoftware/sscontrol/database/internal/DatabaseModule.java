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

import com.anrisoftware.sscontrol.database.external.DatabaseDb;
import com.anrisoftware.sscontrol.database.external.DatabaseUser;
import com.anrisoftware.sscontrol.database.internal.DatabaseAccessImpl.DatabaseAccessImplFactory;
import com.anrisoftware.sscontrol.database.internal.DatabaseDbImpl.DatabaseDbImplFactory;
import com.anrisoftware.sscontrol.database.internal.DatabaseImpl.DatabaseImplFactory;
import com.anrisoftware.sscontrol.database.internal.DatabaseUserImpl.DatabaseUserImplFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class DatabaseModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(DatabaseImpl.class,
                DatabaseImpl.class).build(DatabaseImplFactory.class));
        install(new FactoryModuleBuilder().implement(DatabaseDb.class,
                DatabaseDbImpl.class).build(DatabaseDbImplFactory.class));
        install(new FactoryModuleBuilder().implement(DatabaseUser.class,
                DatabaseUserImpl.class).build(DatabaseUserImplFactory.class));
        install(new FactoryModuleBuilder().implement(DatabaseAccessImpl.class,
                DatabaseAccessImpl.class)
                .build(DatabaseAccessImplFactory.class));
    }

}
