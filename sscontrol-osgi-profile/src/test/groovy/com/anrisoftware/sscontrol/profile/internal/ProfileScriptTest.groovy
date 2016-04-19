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
package com.anrisoftware.sscontrol.profile.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.profile.external.Profile
import com.anrisoftware.sscontrol.profile.external.ProfileProperties
import com.anrisoftware.sscontrol.profile.internal.ProfileImpl.ProfileImplFactory
import com.anrisoftware.sscontrol.types.internal.TypesModule
import com.google.inject.Guice

@Slf4j
@CompileStatic
class ProfileScriptTest {

    @Inject
    ProfileImplFactory profileFactory

    @Test
    void "profile script"() {
        def testCases = [
            [
                input: """
profile 'Ubuntu 12.04' with {
}
profile
""",
                expected: { Profile profile ->
                    assert profile.name == 'Ubuntu 12.04'
                },
            ],
            [
                input: """
profile.with {
    hostname
    hosts
    remote.with { service = 'openssh' }
    mail.with {
        service = 'postfix'
        storage = 'mysql'
        auth = 'sasl'
        delivery = 'courier'
    }
    httpd.with {
        service = ['idapache2': 'apache', 'idproxy': 'nginx']
    }
}
profile
""",
                expected: { Profile profile ->
                    assert profile.entryNames.size() == 5
                    assert profile.entryNames == [
                        'hostname',
                        'hosts',
                        'remote',
                        'mail',
                        'httpd'
                    ]
                    ProfileProperties p
                    p = profile.getEntry('hostname')
                    assert p.name == 'hostname'
                    p = profile.getEntry('remote')
                    assert p.name == 'remote'
                    assert p.propertyNames.size() == 1
                    assert p.propertyNames.containsAll(['service'])
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            log.info '{}. case: {}', k, test
            def profile = Eval.me 'profile', profileFactory.create(), test.input as String
            log.info '{}. case: profile: {}', k, profile
            Closure expected = test.expected
            expected profile
        }
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new ProfileModule(),
                new TypesModule()).injectMembers(this)
    }
}
