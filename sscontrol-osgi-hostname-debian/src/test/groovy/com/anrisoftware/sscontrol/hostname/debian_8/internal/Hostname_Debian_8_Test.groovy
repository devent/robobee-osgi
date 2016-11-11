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
package com.anrisoftware.sscontrol.hostname.debian_8.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.hostname.debian_8.external.Hostname_Debian_8_Factory
import com.anrisoftware.sscontrol.hostname.debian_8.internal.Hostname_Debian_8
import com.anrisoftware.sscontrol.hostname.internal.HostnameModule
import com.anrisoftware.sscontrol.hostname.internal.HostnameImpl.HostnameImplFactory
import com.anrisoftware.sscontrol.services.internal.HostServicesModule
import com.anrisoftware.sscontrol.shell.external.Cmd
import com.anrisoftware.sscontrol.shell.external.utils.ScriptTestBase
import com.anrisoftware.sscontrol.shell.internal.ssh.CmdImpl;
import com.anrisoftware.sscontrol.shell.internal.ssh.CmdModule;
import com.anrisoftware.sscontrol.shell.internal.ssh.CmdRunCaller;
import com.anrisoftware.sscontrol.shell.internal.ssh.ShellModule;
import com.anrisoftware.sscontrol.types.external.HostServiceScript
import com.anrisoftware.sscontrol.types.external.HostServices
import com.google.inject.AbstractModule
import com.google.inject.assistedinject.FactoryModuleBuilder

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@CompileStatic
class Hostname_Debian_8_Test extends ScriptTestBase {

    static final URL sudoOutExpected = Hostname_Debian_8_Test.class.getResource('sudo_out_expected.txt')

    static final URL hostnamectlOutExpected = Hostname_Debian_8_Test.class.getResource('hostnamectl_out_expected.txt')

    static final URL aptgetOutExpected = Hostname_Debian_8_Test.class.getResource('apt-get_out_expected.txt')

    @Inject
    HostnameImplFactory hostnameFactory

    @Inject
    Hostname_Debian_8_Factory hostnameDebianFactory

    @Inject
    CmdRunCaller cmdRunCaller

    @Test
    void "hostname script"() {
        def testCases = [
            [
                input: """
service "hostname" with {
    // Sets the hostname.
    set fqdn: "blog.muellerpublic.de"
}
""",
                expected: { Map args ->
                    File dir = args.dir as File
                    assertStringContent fileToString(new File(dir, 'sudo.out')), resourceToString(sudoOutExpected)
                    assertStringContent fileToString(new File(dir, 'apt-get.out')), resourceToString(aptgetOutExpected)
                    assertStringContent fileToString(new File(dir, 'hostnamectl.out')), resourceToString(hostnamectlOutExpected)
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            doTest test, k
        }
    }

    String getServiceName() {
        'hostname'
    }

    void createDummyCommands(File dir) {
        createEchoCommands dir, [
            'sudo',
            'apt-get',
            'hostnamectl'
        ]
        createBashCommand dir
    }

    void putServices(HostServices services) {
        services.putAvailableService 'hostname', hostnameFactory
        services.putAvailableScriptService 'hostname/debian/8', hostnameDebianFactory
    }

    List getAdditionalModules() {
        [
            new HostnameModule(),
            new HostServicesModule(),
            new ShellModule(),
            new CmdModule(),
            new AbstractModule() {

                @Override
                protected void configure() {
                    bind Cmd to CmdImpl
                    install(new FactoryModuleBuilder().implement(HostServiceScript, Hostname_Debian_8).build(Hostname_Debian_8_Factory))
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
