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
package com.anrisoftware.sscontrol.profile.internal;

import static com.google.inject.util.Providers.of;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.propertiesutils.TypedAllPropertiesFactory;
import com.anrisoftware.propertiesutils.TypedAllPropertiesService;
import com.anrisoftware.sscontrol.profile.internal.ProfilePropertiesImpl.ProfilePropertiesImplFactory;
import com.anrisoftware.sscontrol.types.external.ProfileProperties;
import com.anrisoftware.sscontrol.types.external.ProfilePropertiesService;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

/**
 * Profile properties service.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Component
@Service(ProfilePropertiesService.class)
public class ProfilePropertiesServiceImpl implements ProfilePropertiesService {

    @Inject
    private ProfilePropertiesImplFactory factory;

    @Reference
    private TypedAllPropertiesService typedAllPropertiesService;

    @Override
    public ProfileProperties create() {
        return factory.create();
    }

    @Activate
    protected void start() {
        Guice.createInjector(new ProfileModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(TypedAllPropertiesFactory.class)
                        .toProvider(of(typedAllPropertiesService));
            }
        }).injectMembers(this);
    }
}
