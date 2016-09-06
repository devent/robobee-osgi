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
package com.anrisoftware.sscontrol.dhclient.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.dhclient.external.Dhclient
import com.anrisoftware.sscontrol.dhclient.internal.DhclientImpl.DhclientImplFactory
import com.anrisoftware.sscontrol.profile.internal.ProfileModule
import com.anrisoftware.sscontrol.profile.internal.HostServicePropertiesImpl.HostServicePropertiesImplFactory
import com.anrisoftware.sscontrol.types.external.HostPropertiesService
import com.anrisoftware.sscontrol.types.internal.TypesModule
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
class DhclientScriptTest {

    @Inject
    DhclientImplFactory dhclientFactory

    @Test
    void "dhclient script"() {
        def testCases = [
            [
                input: """
dhclient.with {
    option 'rfc3442-classless-static-routes code 121 = array of unsigned integer 8'
    send 'host-name', 'andare.fugue.com';
    send 'host-name = gethostname()'
    request '!domain-name-servers'
    prepend 'domain-name-servers', '127.0.0.1'
    declare 'interface', 'eth0' with {
        // interface eth0
    }
    declare 'alias' with {
        // alias
    }
    declare 'lease' with {
        // lease
    }
}
dhclient
""",
                expected: { Dhclient dhclient ->
                    assert dhclient.statements.size() == 8
                },
            ],
            [
                input: """
dhclient.with {
    property << 'default_option = rfc3442-classless-static-routes code 121 = array of unsigned integer 8'
    property << 'default_sends = host-name "<hostname>"'
    property << 'default_sends = host-name "my.hostname"'
}
dhclient
""",
                expected: { Dhclient dhclient ->
                    assert dhclient.statements.size() == 0
                    assert dhclient.serviceProperties.propertyNames.size() == 2
                    assert dhclient.serviceProperties.getProperty('default_sends') == 'host-name "my.hostname"'
                    assert dhclient.serviceProperties.propertyNames.containsAll([
                        'default_option',
                        'default_sends'
                    ])
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            log.info '{}. case: {}', k, test
            def database = Eval.me 'dhclient', dhclientFactory.create([:]), test.input as String
            log.info '{}. case: dhclient: {}', k, database
            Closure expected = test.expected
            expected database
        }
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new DhclientModule(),
                new TypesModule(),
                new StringsModule(),
                new ProfileModule(),
                new PropertiesUtilsModule(),
                new AbstractModule() {

                    @Override
                    protected void configure() {
                        bind(HostPropertiesService).to(HostServicePropertiesImplFactory)
                    }
                }
                ).injectMembers(this)
    }
}
