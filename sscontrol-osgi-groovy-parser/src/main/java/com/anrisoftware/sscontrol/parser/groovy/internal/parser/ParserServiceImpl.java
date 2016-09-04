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
package com.anrisoftware.sscontrol.parser.groovy.internal.parser;

import static com.google.inject.util.Providers.of;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.BundleContext;

import com.anrisoftware.sscontrol.parser.external.Parser;
import com.anrisoftware.sscontrol.parser.external.ParserService;
import com.anrisoftware.sscontrol.parser.groovy.internal.parser.ParserImpl.ParserImplFactory;
import com.anrisoftware.sscontrol.types.external.HostServices;
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

    @Override
    public Parser create(URI[] roots, String name, HostServices hostServices) {
        Map<String, Object> variables = new HashMap<String, Object>();
        return scriptsFactory.create(roots, name, variables, hostServices);
    }

    @Override
    public Parser create(URI[] roots, String name,
            Map<String, Object> variables, HostServices hostServices) {
        return scriptsFactory.create(roots, name, variables, hostServices);
    }

    @Activate
    protected void start(final BundleContext bundleContext) {
        Guice.createInjector(new ParserModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(BundleContext.class).toProvider(of(bundleContext));
            }
        }).injectMembers(this);
    }
}
