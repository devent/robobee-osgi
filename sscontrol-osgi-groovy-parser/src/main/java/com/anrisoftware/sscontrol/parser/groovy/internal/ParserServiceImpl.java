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
package com.anrisoftware.sscontrol.parser.groovy.internal;

import static com.google.inject.util.Providers.of;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.BundleContext;

import com.anrisoftware.sscontrol.parser.external.Parser;
import com.anrisoftware.sscontrol.parser.external.ParserService;
import com.anrisoftware.sscontrol.parser.groovy.internal.ParserImpl.ParserImplFactory;
import com.anrisoftware.sscontrol.types.external.ToStringService;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

/**
 * Groovy script parser service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(ParserService.class)
public class ParserServiceImpl implements ParserService {

    @Inject
    private ParserImplFactory scriptsFactory;

    @Reference
    private ToStringService toStringService;

    @Override
    public Parser create() {
        return scriptsFactory.create();
    }

    @Activate
    protected void start(final BundleContext bundleContext) {
        Guice.createInjector(new ParserModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(BundleContext.class).toProvider(of(bundleContext));
                bind(ToStringService.class).toProvider(of(toStringService));
            }
        }).injectMembers(this);
    }
}
