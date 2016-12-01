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
package com.anrisoftware.sscontrol.hosts.linux.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokensTemplateModule
import com.anrisoftware.sscontrol.hosts.internal.HostsModule
import com.anrisoftware.sscontrol.hosts.internal.HostsImpl.HostsImplFactory
import com.anrisoftware.sscontrol.hosts.linux.external.Hosts_Linux_Factory
import com.anrisoftware.sscontrol.replace.internal.ReplaceModule
import com.anrisoftware.sscontrol.services.internal.HostServicesModule
import com.anrisoftware.sscontrol.shell.external.Cmd
import com.anrisoftware.sscontrol.shell.external.utils.AbstractScriptTestBase
import com.anrisoftware.sscontrol.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.shell.internal.copy.CopyModule
import com.anrisoftware.sscontrol.shell.internal.fetch.FetchModule
import com.anrisoftware.sscontrol.shell.internal.scp.ScpModule
import com.anrisoftware.sscontrol.shell.internal.ssh.CmdImpl
import com.anrisoftware.sscontrol.shell.internal.ssh.CmdRunCaller
import com.anrisoftware.sscontrol.shell.internal.ssh.ShellModule
import com.anrisoftware.sscontrol.shell.internal.ssh.SshModule
import com.anrisoftware.sscontrol.types.external.HostServices
import com.google.inject.AbstractModule

import groovy.util.logging.Slf4j

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class Hosts_Linux_Test extends AbstractScriptTestBase {

    @Inject
    HostsImplFactory hostsFactory

    @Inject
    Hosts_Linux_Factory hostsLinuxFactory

    @Inject
    CmdRunCaller cmdRunCaller

    static Map expectedResources = [
        explicit_list_sudo: Hosts_Linux_Test.class.getResource('explicit_list_sudo_expected.txt'),
        explicit_list_chown: Hosts_Linux_Test.class.getResource('explicit_list_chown_expected.txt'),
        explicit_list_cp: Hosts_Linux_Test.class.getResource('explicit_list_cp_expected.txt'),
        explicit_list_rm: Hosts_Linux_Test.class.getResource('explicit_list_rm_expected.txt'),
        explicit_list_chown: Hosts_Linux_Test.class.getResource('explicit_list_chown_expected.txt'),
        explicit_list_chmod: Hosts_Linux_Test.class.getResource('explicit_list_chmod_expected.txt'),
    ]

    @Test
    void "hosts script"() {
        def testCases = [
            [
                name: "explicit_list",
                input: """
service "hosts" with {
    ip "192.168.0.52", host: "srv1.ubuntutest.com"
    ip "192.168.0.49", host: "srv1.ubuntutest.de", alias: "srv1"
}
""",
                expected: { Map args ->
                    File dir = args.dir
                    assertStringContent fileToStringReplace(new File(dir, 'sudo.out')), resourceToString(expectedResources["${args.test.name}_sudo"])
                    assertStringContent fileToStringReplace(new File(dir, 'chown.out')), resourceToString(expectedResources["${args.test.name}_chown"])
                    assertStringContent fileToStringReplace(new File(dir, 'cp.out')), resourceToString(expectedResources["${args.test.name}_cp"])
                    assertStringContent fileToStringReplace(new File(dir, 'rm.out')), resourceToString(expectedResources["${args.test.name}_rm"])
                    assertStringContent fileToStringReplace(new File(dir, 'chown.out')), resourceToString(expectedResources["${args.test.name}_chown"])
                    assertStringContent fileToStringReplace(new File(dir, 'chmod.out')), resourceToString(expectedResources["${args.test.name}_chmod"])
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            doTest test, k
        }
    }

    String getServiceName() {
        'hosts'
    }

    String getScriptServiceName() {
        'hosts/linux/0'
    }

    void createDummyCommands(File dir) {
        createEchoCommands dir, [
            'mkdir',
            'chown',
            'chmod',
            'cp',
            'rm',
            'sudo',
            'scp',
        ]
    }

    void putServices(HostServices services) {
        services.putAvailableService 'hosts', hostsFactory
        services.putAvailableScriptService 'hosts/linux/0', hostsLinuxFactory
    }

    List getAdditionalModules() {
        [
            new HostsModule(),
            new Hosts_Linux_Module(),
            new HostServicesModule(),
            new ShellModule(),
            new SshModule(),
            new CmdModule(),
            new ScpModule(),
            new CopyModule(),
            new FetchModule(),
            new ReplaceModule(),
            new TokensTemplateModule(),
            new AbstractModule() {

                @Override
                protected void configure() {
                    bind Cmd to CmdImpl
                }
            }
        ]
    }

    @Before
    void setupTest() {
        toStringStyle
        injector = createInjector()
        injector.injectMembers(this)
        this.threads = createThreads()
    }
}
