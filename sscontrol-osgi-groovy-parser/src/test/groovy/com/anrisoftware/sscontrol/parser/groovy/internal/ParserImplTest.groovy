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
package com.anrisoftware.sscontrol.parser.groovy.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.parser.groovy.internal.HostnameStub.HostnamePreScriptImpl
import com.anrisoftware.sscontrol.parser.groovy.internal.HostnameStub.HostnameStubFactory
import com.anrisoftware.sscontrol.parser.groovy.internal.HostnameStub.HostnamePreScriptImpl.HostnamePreScriptImplFactory
import com.anrisoftware.sscontrol.parser.groovy.internal.parser.ParserModule
import com.anrisoftware.sscontrol.parser.groovy.internal.parser.ParserImpl.ParserImplFactory
import com.anrisoftware.sscontrol.services.internal.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.TargetsModule
import com.anrisoftware.sscontrol.services.internal.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.TargetsImpl.TargetsImplFactory
import com.anrisoftware.sscontrol.types.external.HostService
import com.anrisoftware.sscontrol.types.external.PreHost
import com.anrisoftware.sscontrol.types.external.TargetsService
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.assistedinject.FactoryModuleBuilder

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
@CompileStatic
class ParserImplTest {

    @Inject
    ParserImplFactory scriptsFactory

    @Inject
    HostServicesImplFactory servicesFactory

    @Inject
    HostnameStubFactory hostnameFactory

    @Inject
    HostnamePreScriptImplFactory hostnamePreFactory

    @Test
    void "parse script"() {
        def parent = hostnameScript.toString()
        int index = parent.lastIndexOf '/'
        parent = parent.substring(0, index + 1)
        def roots = [new URI(parent)] as URI[]
        def name = 'HostnameScript.groovy'
        def variables = [:]
        def hostServices = servicesFactory.create()
        hostServices.putAvailableService 'hostname', hostnameFactory
        hostServices.putAvailablePreService 'hostname', hostnamePreFactory
        def parser = scriptsFactory.create(roots, name, variables, hostServices)
        parser.parse()
        assert hostServices.services.size() == 1
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new ParserModule(),
                new HostServicesModule(),
                new TargetsModule(),
                new AbstractModule() {

                    @Override
                    protected void configure() {
                        bind TargetsService to TargetsImplFactory
                        install(new FactoryModuleBuilder().implement(HostService.class, HostnameStub.class).build(HostnameStubFactory.class));
                        install(new FactoryModuleBuilder().implement(PreHost.class, HostnamePreScriptImpl.class).build(HostnamePreScriptImplFactory.class));
                    }
                }).injectMembers(this)
    }

    static final URI hostnameScript = ParserImplTest.class.getResource('HostnameScript.groovy').toURI()
}
