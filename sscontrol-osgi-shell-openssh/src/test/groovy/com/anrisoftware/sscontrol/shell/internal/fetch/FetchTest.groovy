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
package com.anrisoftware.sscontrol.shell.internal.fetch

import static com.anrisoftware.globalpom.utils.TestUtils.assertStringContent
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.sscontrol.fetch.external.Fetch
import com.anrisoftware.sscontrol.fetch.external.Fetch.FetchFactory
import com.anrisoftware.sscontrol.shell.external.utils.AbstractCmdTestBase
import com.anrisoftware.sscontrol.shell.external.utils.CmdUtilsModules
import com.anrisoftware.sscontrol.shell.external.utils.SshFactory
import com.anrisoftware.sscontrol.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.shell.internal.scp.ScpModule
import com.anrisoftware.sscontrol.shell.internal.ssh.SshModule
import com.google.inject.Module

import groovy.util.logging.Slf4j

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class FetchTest extends AbstractCmdTestBase {

    static Threads threads

    @Inject
    FetchFactory fetchFactory

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    static Map expectedResources = [
        src_scp: FetchTest.class.getResource('src_scp_expected.txt'),
        src_sudo: FetchTest.class.getResource('src_sudo_expected.txt'),
        directory_src_scp: FetchTest.class.getResource('directory_src_scp_expected.txt'),
        directory_src_sudo: FetchTest.class.getResource('directory_src_sudo_expected.txt'),
        dest_src_scp: FetchTest.class.getResource('dest_src_scp_expected.txt'),
        dest_src_sudo: FetchTest.class.getResource('dest_src_sudo_expected.txt'),
        privileged_src_scp: FetchTest.class.getResource('privileged_src_scp_expected.txt'),
        privileged_src_sudo: FetchTest.class.getResource('privileged_src_sudo_expected.txt'),
    ]

    @Test
    void "fetch cases"() {
        def testCases = [
            [
                enabled: true,
                name: "src",
                args: [
                    src: "aaa.txt",
                    dest: null,
                ],
                expected: { Map args ->
                    File dir = args.dir as File
                    String name = args.name as String
                    assertStringContent fileToString(new File(dir, 'scp.out')), resourceToString(expectedResources["${name}_scp"] as URL)
                    assertStringContent fileToString(new File(dir, 'sudo.out')), resourceToString(expectedResources["${name}_sudo"] as URL)
                },
            ],
            [
                enabled: true,
                name: "directory_src",
                args: [
                    src: "/var/wordpress",
                    dest: null,
                    recursive: true,
                ],
                expected: { Map args ->
                    File dir = args.dir as File
                    String name = args.name as String
                    assertStringContent fileToString(new File(dir, 'scp.out')), resourceToString(expectedResources["${name}_scp"] as URL)
                    assertStringContent fileToString(new File(dir, 'sudo.out')), resourceToString(expectedResources["${name}_sudo"] as URL)
                },
            ],
            [
                enabled: true,
                name: "dest_src",
                args: [
                    src: "aaa.txt",
                    dest: "/tmp",
                ],
                expected: { Map args ->
                    File dir = args.dir as File
                    String name = args.name as String
                    assertStringContent fileToString(new File(dir, 'scp.out')), resourceToString(expectedResources["${name}_scp"] as URL)
                },
            ],
            [
                enabled: true,
                name: "privileged_src",
                args: [
                    src: "aaa.txt",
                    dest: "/tmp",
                    privileged: true,
                ],
                expected: { Map args ->
                    File dir = args.dir as File
                    String name = args.name as String
                    assertStringContent fileToString(new File(dir, 'scp.out')), resourceToString(expectedResources["${name}_scp"] as URL)
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            if (test.enabled) {
                log.info '\n######### {}. case: {}', k, test
                def tmp = folder.newFolder()
                test.host = SshFactory.localhost(injector).hosts[0]
                doTest test, tmp, k
            }
        }
    }

    def createCmd(Map test, File tmp, int k) {
        def fetch = fetchFactory.create test.args, test.host, this, threads, log
        createEchoCommands tmp, [
            'mkdir',
            'chown',
            'chmod',
            'cp',
            'rm',
            'sudo',
            'scp',
        ]
        return fetch
    }

    @Before
    void setupTest() {
        super.setupTest()
        this.threads = CmdUtilsModules.getThreads(injector)
    }

    Module[] getAdditionalModules() {
        [
            new CmdModule(),
            new SshModule(),
            new FetchModule(),
            new ScpModule(),
            new CmdUtilsModules(),
        ] as Module[]
    }
}
