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
package com.anrisoftware.sscontrol.hosts.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.google.inject.util.Providers.of
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.hosts.external.Host
import com.anrisoftware.sscontrol.hosts.external.Hosts
import com.anrisoftware.sscontrol.hosts.internal.HostsImpl.HostsImplFactory
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesImpl.HostServicePropertiesImplFactory
import com.anrisoftware.sscontrol.services.internal.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.TargetsModule
import com.anrisoftware.sscontrol.services.internal.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.TargetsImpl.TargetsImplFactory
import com.anrisoftware.sscontrol.types.external.HostPropertiesService
import com.anrisoftware.sscontrol.types.external.HostServices
import com.anrisoftware.sscontrol.types.external.TargetsService
import com.google.inject.AbstractModule
import com.google.inject.Guice

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@CompileStatic
class HostsScriptTest {

    @Inject
    HostServicesImplFactory servicesFactory

    @Inject
    HostsImplFactory hostnameFactory

    @Test
    void "hosts service"() {
        def testCases = [
            [
                input: """
service "hosts" with {
    ip "192.168.0.52", host: "srv1.ubuntutest.com"
    ip "192.168.0.49", host: "srv1.ubuntutest.de", alias: "srv1"
}
""",
                expected: { HostServices services ->
                    assert services.getServices('hosts').size() == 1
                    Hosts hosts = services.getServices('hosts')[0] as Hosts
                    assert hosts.hosts.size() == 2
                    Host h = hosts.hosts[0]
                    assert h.address == "192.168.0.52"
                    assert h.host == "srv1.ubuntutest.com"
                    assert h.aliases.size() == 0
                    h = hosts.hosts[1]
                    assert h.address == "192.168.0.49"
                    assert h.host == "srv1.ubuntutest.de"
                    assert h.aliases.containsAll(["srv1"])
                },
            ],
            [
                input: """
service "hosts", ip: "192.168.0.52", host: "srv1.ubuntutest.com", alias: "srv1"
""",
                expected: { HostServices services ->
                    assert services.getServices('hosts').size() == 1
                    Hosts hosts = services.getServices('hosts')[0] as Hosts
                    assert hosts.hosts.size() == 1
                    Host h = hosts.hosts[0]
                    assert h.address == "192.168.0.52"
                    assert h.host == "srv1.ubuntutest.com"
                    assert h.aliases.containsAll(["srv1"])
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            log.info '{}. case: {}', k, test
            def services = servicesFactory.create()
            services.putAvailableService 'hosts', hostnameFactory
            Eval.me 'service', services, test.input as String
            Closure expected = test.expected
            expected services
        }
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new HostsModule(),
                new HostServicesModule(),
                new TargetsModule(),
                new PropertiesModule(),
                new PropertiesUtilsModule(),
                new AbstractModule() {

                    @Override
                    protected void configure() {
                        bind TargetsService to TargetsImplFactory
                        bind(HostPropertiesService).to(HostServicePropertiesImplFactory)
                    }
                }).injectMembers(this)
    }
}
