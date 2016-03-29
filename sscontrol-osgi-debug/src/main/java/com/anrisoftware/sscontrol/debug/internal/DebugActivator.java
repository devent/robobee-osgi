/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-debug.
 *
 * sscontrol-osgi-debug is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-debug is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-debug. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.debug.internal;

import static com.google.inject.Guice.createInjector;

import java.util.Dictionary;
import java.util.Properties;

import javax.inject.Inject;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.anrisoftware.sscontrol.debug.external.DebugLogging;

/**
 * Extension of the default OSGi bundle activator
 */
public class DebugActivator implements BundleActivator {

    @Inject
    private DebugLoggingImpl debug;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void start(BundleContext bc) throws Exception {
        createInjector(new DebugServicesModule(bc)).injectMembers(this);
        Dictionary props = new Properties();
        bc.registerService(DebugLogging.class.getName(), debug, props);
    }

    @Override
    public void stop(BundleContext bc) throws Exception {
    }
}