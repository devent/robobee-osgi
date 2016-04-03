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

import org.osgi.framework.BundleContext;

import com.anrisoftware.sscontrol.types.external.ToStringService;
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
        bind(UserPasswordService.class).toProvider(
                of((UserPasswordService) context.getService(context
                        .getServiceReference(UserPasswordService.class
                                .getName()))));
        bind(ToStringService.class)
                .toProvider(
                        of((ToStringService) context.getService(context
                                .getServiceReference(ToStringService.class
                                        .getName()))));
    }

}
