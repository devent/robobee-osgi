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
import com.anrisoftware.sscontrol.services.internal.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.TargetsModule
import com.anrisoftware.sscontrol.services.internal.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.TargetsImpl.TargetsImplFactory
import com.anrisoftware.sscontrol.ssh.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.types.external.HostServices
import com.anrisoftware.sscontrol.types.external.Ssh
import com.anrisoftware.sscontrol.types.external.SshHost
import com.anrisoftware.sscontrol.types.external.TargetsService
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
    group "nodes"
}
ssh
""",
                expected: { Ssh ssh ->
                    assert ssh.group == 'nodes'
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
            def ssh = Eval.me 'ssh', sshFactory.create([:]), test.input as String
            log.info '{}. case: ssh: {}', k, ssh
            Closure expected = test.expected
            expected ssh
        }
    }

    @Inject
    HostServicesImplFactory servicesFactory

    @Test
    void "ssh service"() {
        def testCases = [
            [
                input: """
service "ssh" with {
    host "192.168.0.2"
}
""",
                expected: { HostServices services ->
                    assert services.getServices('ssh').size() == 1
                    Ssh ssh = services.getServices('ssh')[0] as Ssh
                    assert ssh.hosts.size() == 1
                    SshHost host = ssh.hosts[0]
                    assert host.host == "192.168.0.2"
                },
            ],
            [
                input: """
service "ssh", group: "master" with {
    host "192.168.0.2"
}
""",
                expected: { HostServices services ->
                    assert services.getServices('ssh').size() == 1
                    Ssh ssh = services.getServices('ssh')[0] as Ssh
                    assert ssh.group == 'master'
                    assert ssh.hosts.size() == 1
                    SshHost host = ssh.hosts[0]
                    assert host.host == "192.168.0.2"
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            log.info '{}. case: {}', k, test
            def services = servicesFactory.create()
            services.putAvailableService 'ssh', sshFactory
            Eval.me 'service', services, test.input as String
            Closure expected = test.expected
            expected services
        }
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new SshModule(),
                new DebugLoggingModule(),
                new TypesModule(),
                new StringsModule(),
                new HostServicesModule(),
                new TargetsModule(),
                new AbstractModule() {

                    @Override
                    protected void configure() {
                        bind TargetsService to TargetsImplFactory
                    }
                }).injectMembers(this)
    }
}
