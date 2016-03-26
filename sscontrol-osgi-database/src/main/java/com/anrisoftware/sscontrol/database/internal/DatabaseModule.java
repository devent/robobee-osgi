/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-database.
 *
 * sscontrol-osgi-database is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-database is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-database. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.internal;

import com.anrisoftware.sscontrol.database.external.Database;
import com.anrisoftware.sscontrol.database.external.DatabaseDb;
import com.anrisoftware.sscontrol.database.external.DatabaseUser;
import com.anrisoftware.sscontrol.database.internal.DatabaseAccessImpl.DatabaseAccessImplFactory;
import com.anrisoftware.sscontrol.database.internal.DatabaseDbImpl.DatabaseDbImplFactory;
import com.anrisoftware.sscontrol.database.internal.DatabaseImpl.DatabaseImplFactory;
import com.anrisoftware.sscontrol.database.internal.DatabaseUserImpl.DatabaseUserImplFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class DatabaseModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Database.class).to(DatabaseImpl.class);
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
