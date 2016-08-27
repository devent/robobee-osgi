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
package com.anrisoftware.sscontrol.services.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.strings.StringsModule
import com.anrisoftware.sscontrol.services.internal.HostServiceStub.HostServiceStubFactory
import com.anrisoftware.sscontrol.services.internal.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.types.external.HostServices
import com.anrisoftware.sscontrol.types.internal.TypesModule
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.assistedinject.FactoryModuleBuilder

/**
 * 
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@CompileStatic
class HostServicesImplTest {

    Injector injector

    @Inject
    HostServicesImplFactory servicesFactory

    @Inject
    HostServiceStubServiceImpl serviceService

    @Test
    void "load mock service"() {
        def testCases = [
            [
                input: """
service "mock" with {
    // Sets the hostname.
    set "blog.muellerpublic.de"
}
""",
                expected: { HostServices services ->
                    assert services.getServices('mock').size() == 1
                    HostServiceStub service = services.getServices('mock')[0] as HostServiceStub
                    assert service.name == 'blog.muellerpublic.de'
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            log.info '{}. case: {}', k, test
            def services = servicesFactory.create()
            services.putAvailableService 'mock', serviceService
            Eval.me 'service', services, test.input as String
            Closure expected = test.expected
            expected services
        }
    }

    @Before
    void setupTest() {
        toStringStyle
        this.injector = Guice.createInjector(
                new HostServicesModule(),
                new TypesModule(),
                new StringsModule(),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        install(new FactoryModuleBuilder().implement(
                                HostServiceStub.class, HostServiceStub.class)
                                .build(HostServiceStubFactory.class));
                    }
                })
        injector.injectMembers(this)
    }
}
