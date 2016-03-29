/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-dhclient.
 *
 * sscontrol-osgi-dhclient is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-dhclient is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-dhclient. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dhclient.internal;

import com.anrisoftware.sscontrol.dhclient.external.Dhclient;
import com.anrisoftware.sscontrol.dhclient.internal.DhclientImpl.DhclientImplFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class DhclientModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Dhclient.class).to(DhclientImpl.class);
        install(new FactoryModuleBuilder().implement(DhclientImpl.class,
                DhclientImpl.class).build(DhclientImplFactory.class));
    }

}
