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
package com.anrisoftware.sscontrol.types.groovy.internal;

import static com.google.inject.util.Providers.of;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.types.external.BindingHost;
import com.anrisoftware.sscontrol.types.external.BindingHostService;
import com.anrisoftware.sscontrol.types.external.ToStringService;
import com.anrisoftware.sscontrol.types.groovy.internal.BindingHostImpl.BindingHostImplFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

/**
 * Binding host and port service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(BindingHostService.class)
public class BindingHostServiceImpl implements BindingHostService {

    @Reference
    private ToStringService toStringService;

    @Inject
    private BindingHostImplFactory bindingHostFactory;

    @Override
    public BindingHost create() {
        return bindingHostFactory.create();
    }

    @Override
    public BindingHost create(BindingHost binding) {
        return bindingHostFactory.create(binding);
    }

    @Activate
    protected void start() {
        Guice.createInjector(new GroovyTypesModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(ToStringService.class).toProvider(of(toStringService));
            }
        }).injectMembers(this);
    }

}
