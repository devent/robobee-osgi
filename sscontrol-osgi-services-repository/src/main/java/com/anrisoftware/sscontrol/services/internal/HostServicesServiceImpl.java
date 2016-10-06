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
package com.anrisoftware.sscontrol.services.internal;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.services.internal.HostServicesImpl.HostServicesImplFactory;
import com.anrisoftware.sscontrol.types.external.HostServices;
import com.anrisoftware.sscontrol.types.external.HostServicesService;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

/**
 * Creates the host services repository.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component(immediate = true)
@Service(HostServicesService.class)
public class HostServicesServiceImpl implements HostServicesService {

    @Inject
    private HostServicesImplFactory repositoryFactory;

    private HostServices scriptsRepository;

    @Override
    public synchronized HostServices create() {
        if (scriptsRepository == null) {
            this.scriptsRepository = repositoryFactory.create();
        }
        return scriptsRepository;
    }

    @Activate
    protected void start() {
        Guice.createInjector(new HostServicesModule(),
                new AbstractModule() {

                    @Override
                    protected void configure() {
                    }
                }).injectMembers(this);
    }
}
