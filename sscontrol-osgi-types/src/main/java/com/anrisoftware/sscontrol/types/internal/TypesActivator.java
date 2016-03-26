/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-types.
 *
 * sscontrol-osgi-types is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-types is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-types. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.types.internal;

import java.util.Properties;

import javax.inject.Inject;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.anrisoftware.sscontrol.types.external.UserPasswordService;
import com.google.inject.Guice;

/**
 * Extension of the default OSGi bundle activator
 */
public class TypesActivator implements BundleActivator {

    @Inject
    private UserPasswordService userPasswordService;

    @Override
    public void start(BundleContext bc) throws Exception {
        Guice.createInjector(new TypesModule()).injectMembers(this);
        Properties props = new Properties();
        bc.registerService(UserPasswordService.class.getName(),
                userPasswordService, props);
    }

    @Override
    public void stop(BundleContext bc) throws Exception {
    }
}
