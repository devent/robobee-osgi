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
package com.anrisoftware.sscontrol.ssh.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.strings.StringsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.ssh.external.Ssh
import com.anrisoftware.sscontrol.ssh.external.SshHost
import com.anrisoftware.sscontrol.ssh.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.types.internal.TypesModule
import com.google.inject.Guice

@Slf4j
@CompileStatic
class SshScriptTest {

    @Inject
    SshImplFactory sshFactory

    @Test
    void "ssh script"() {
        def testCases = [
            [
                input: """
ssh.with {
    debug "error", facility: 'auth', level: 1
}
ssh
""",
                expected: { Ssh ssh ->
                    assert ssh.debugLogging.modules.size() == 1
                },
            ],
            [
                input: """
ssh.with {
    debug << [name: "error", level: 1]
}
ssh
""",
                expected: { Ssh ssh ->
                    assert ssh.debugLogging.modules.size() == 1
                },
            ],
            [
                input: """
debugLogs = []
debugLogs << [name: "error", level: 1]
debugLogs.each { ssh.debug it }
ssh
""",
                expected: { Ssh ssh ->
                    assert ssh.debugLogging.modules.size() == 1
                },
            ],
            [
                input: """
ssh.with {
    host "user"
    host "user@192.168.0.2"
    host "user@192.168.0.3:22"
}
ssh
""",
                expected: { Ssh ssh ->
                    assert ssh.hosts.size() == 3
                    int i = 0
                    SshHost host = ssh.hosts[i++]
                    assert host.user == null
                    assert host.host == 'user'
                    assert host.port == null
                    assert host.key == null
                    host = ssh.hosts[i++]
                    assert host.user == 'user'
                    assert host.host == '192.168.0.2'
                    assert host.port == null
                    assert host.key == null
                    host = ssh.hosts[i++]
                    assert host.user == 'user'
                    assert host.host == '192.168.0.3'
                    assert host.port == 22
                    assert host.key == null
                },
            ],
            [
                input: """
ssh.with {
    host << "192.168.0.1"
    host << "192.168.0.2"
    host << "192.168.0.3"
}
ssh
""",
                expected: { Ssh ssh ->
                    assert ssh.hosts.size() == 3
                    int i = 0
                    SshHost host = ssh.hosts[i++]
                    assert host.user == null
                    assert host.host == '192.168.0.1'
                    assert host.port == null
                    assert host.key == null
                    host = ssh.hosts[i++]
                    assert host.user == null
                    assert host.host == '192.168.0.2'
                    assert host.port == null
                    assert host.key == null
                    host = ssh.hosts[i++]
                    assert host.user == null
                    assert host.host == '192.168.0.3'
                    assert host.port == null
                    assert host.key == null
                },
            ],
            [
                input: """
sshHosts = []
sshHosts << [host: "192.168.0.1", user: "user", key: "user.pub"]
sshHosts << [host: "192.168.0.2", user: "user", key: "user.pub"]
sshHosts << [host: "192.168.0.3", user: "user", key: "user.pub"]
sshHosts.each { ssh.host it }
ssh
""",
                expected: { Ssh ssh ->
                    assert ssh.hosts.size() == 3
                    int i = 0
                    SshHost host = ssh.hosts[i++]
                    assert host.user == 'user'
                    assert host.host == '192.168.0.1'
                    assert host.port == null
                    assert host.key == new URI('file://user.pub')
                    host = ssh.hosts[i++]
                    assert host.user == 'user'
                    assert host.host == '192.168.0.2'
                    assert host.port == null
                    assert host.key == new URI('file://user.pub')
                    host = ssh.hosts[i++]
                    assert host.user == 'user'
                    assert host.host == '192.168.0.3'
                    assert host.port == null
                    assert host.key == new URI('file://user.pub')
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            log.info '{}. case: {}', k, test
            def ssh = Eval.me 'ssh', sshFactory.create(), test.input as String
            log.info '{}. case: ssh: {}', k, ssh
            Closure expected = test.expected
            expected ssh
        }
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new SshModule(),
                new DebugLoggingModule(),
                new TypesModule(),
                new StringsModule()).injectMembers(this)
    }
}
