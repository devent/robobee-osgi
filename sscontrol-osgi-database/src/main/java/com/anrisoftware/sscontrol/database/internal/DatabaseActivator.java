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

import static com.google.inject.Guice.createInjector;

import java.util.Dictionary;
import java.util.Properties;

import javax.inject.Inject;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.anrisoftware.sscontrol.database.external.Database;

/**
 * Extension of the default OSGi bundle activator
 */
public class DatabaseActivator implements BundleActivator {

    @Inject
    private DatabaseImpl database;

    @SuppressWarnings("rawtypes")
    @Override
    public void start(BundleContext bc) throws Exception {
        createInjector(new DatabaseServicesModule(bc)).injectMembers(this);
        Dictionary props = new Properties();
        bc.registerService(Database.class.getName(), database, props);
    }

    @Override
    public void stop(BundleContext bc) throws Exception {
    }
}
