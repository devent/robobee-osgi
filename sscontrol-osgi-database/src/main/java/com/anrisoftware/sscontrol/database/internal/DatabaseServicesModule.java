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

import static com.google.inject.util.Providers.of;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.anrisoftware.sscontrol.types.external.UserPasswordService;
import com.google.inject.AbstractModule;

public class DatabaseServicesModule extends AbstractModule {

    private final BundleContext context;

    public DatabaseServicesModule(BundleContext bc) {
        this.context = bc;
    }

    @Override
    protected void configure() {
        retriveServices();
        install(new DatabaseModule());
    }

    private void retriveServices() {
        ServiceReference reference = context
                .getServiceReference(UserPasswordService.class.getName());
        bind(UserPasswordService.class).toProvider(
                of((UserPasswordService) context.getService(reference)));
    }

}
