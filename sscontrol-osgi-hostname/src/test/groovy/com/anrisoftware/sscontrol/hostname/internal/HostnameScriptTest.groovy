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
package com.anrisoftware.sscontrol.hostname.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.google.inject.util.Providers.of
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.hostname.external.Hostname
import com.anrisoftware.sscontrol.hostname.internal.HostnameImpl.HostnameImplFactory
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
class HostnameScriptTest {

    @Inject
    HostnameImplFactory hostnameFactory

    @Test
    void "hostname script"() {
        def testCases = [
            [
                input: """
hostname.with {
    set 'test.muellerpublic.de'
}
hostname
""",
                expected: { Hostname hostname ->
                    assert hostname.hostname == 'test.muellerpublic.de'
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            log.info '{}. case: {}', k, test
            def hostname = Eval.me 'hostname', hostnameFactory.create(), test.input as String
            log.info '{}. case: hostname: {}', k, hostname
            Closure expected = test.expected
            expected hostname
        }
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new HostnameModule(),
                new AbstractModule() {

                    @Override
                    protected void configure() {
                    }
                }).injectMembers(this)
    }
}
