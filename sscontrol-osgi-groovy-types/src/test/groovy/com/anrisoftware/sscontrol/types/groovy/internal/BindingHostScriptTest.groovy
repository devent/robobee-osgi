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
package com.anrisoftware.sscontrol.types.groovy.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.types.external.BindingHost
import com.anrisoftware.sscontrol.types.external.BindingHost.Host
import com.anrisoftware.sscontrol.types.groovy.internal.BindingHostImpl.BindingHostImplFactory
import com.anrisoftware.sscontrol.types.internal.TypesModule
import com.google.inject.Guice

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
@CompileStatic
class BindingHostScriptTest {

    @Inject
    BindingHostImplFactory bindingFactory

    @Test
    void "binding script"() {
        def testCases = [
            [
                input: """
bindingParent.with {
    binding '192.168.0.1', port: 22
    binding '!192.168.0.2', port: 22
}
bindingParent
""",
                expected: { BindingParent parent ->
                    BindingHost binding = parent.binding
                    assert binding.addedHosts.size() == 1
                    Host host = binding.addedHosts[0]
                    assert host.host == '192.168.0.1'
                    assert host.port == 22
                    assert binding.removedHosts.size() == 1
                    host = binding.removedHosts[0]
                    assert host.host == '192.168.0.2'
                    assert host.port == 22
                },
            ],
            [
                input: """
import static com.anrisoftware.sscontrol.types.external.BindingAddress.*
bindingParent.with {
    binding local, port: 22
}
bindingParent
""",
                expected: { BindingParent parent ->
                    BindingHost binding = parent.binding
                    assert binding.addedHosts.size() == 1
                    assert binding.removedHosts.size() == 0
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            log.info '{}. case: {}', k, test
            def bindingParent = Eval.me 'bindingParent', new BindingParent(binding: bindingFactory.create()), test.input as String
            log.info '{}. case: binding: {}', k, bindingParent
            Closure expected = test.expected
            expected bindingParent
        }
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new TypesModule(),
                new GroovyTypesModule()).injectMembers(this)
    }
}
