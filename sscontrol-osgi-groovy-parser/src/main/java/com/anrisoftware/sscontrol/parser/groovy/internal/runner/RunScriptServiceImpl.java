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
package com.anrisoftware.sscontrol.parser.groovy.internal.runner;

import static com.google.inject.Guice.createInjector;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.parser.external.RunScript;
import com.anrisoftware.sscontrol.parser.external.RunScriptService;
import com.anrisoftware.sscontrol.parser.groovy.internal.runner.RunScriptImpl.RunScriptImplFactory;
import com.anrisoftware.sscontrol.types.external.HostServices;
import com.google.inject.AbstractModule;

/**
 * Script runner service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(RunScriptService.class)
public class RunScriptServiceImpl implements RunScriptService {

    @Inject
    private RunScriptImplFactory runScriptFactory;

    @Override
    public RunScript create(HostServices repository) {
        return runScriptFactory.create(repository);
    }

    @Activate
    protected void start() {
        createInjector(new SscontrolParserModule(), new AbstractModule() {

            @Override
            protected void configure() {
            }
        }).injectMembers(this);
    }

}
