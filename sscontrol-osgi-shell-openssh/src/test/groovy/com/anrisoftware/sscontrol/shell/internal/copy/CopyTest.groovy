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
package com.anrisoftware.sscontrol.shell.internal.copy

import static com.anrisoftware.globalpom.utils.TestUtils.assertStringContent
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.sscontrol.copy.external.Copy.CopyFactory
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
class CopyTest extends AbstractCmdTestBase {

    static Threads threads

    @Inject
    CopyFactory copyFactory

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    static Map expectedResources = [
        dest_src_scp: CopyTest.class.getResource('dest_src_scp_expected.txt'),
        privileged_src_scp: CopyTest.class.getResource('privileged_src_scp_expected.txt'),
        privileged_src_cp: CopyTest.class.getResource('privileged_src_cp_expected.txt'),
        privileged_src_rm: CopyTest.class.getResource('privileged_src_rm_expected.txt'),
    ]

    @Test
    void "copy cases"() {
        def testCases = [
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
                    assertStringContent fileToString(new File(dir, 'cp.out')), resourceToString(expectedResources["${name}_cp"] as URL)
                    assertStringContent fileToString(new File(dir, 'rm.out')), resourceToString(expectedResources["${name}_rm"] as URL)
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
        def fetch = copyFactory.create test.args, test.host, this, threads, log
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
            new SshModule(),
            new CmdModule(),
            new CopyModule(),
            new ScpModule(),
            new CmdUtilsModules(),
        ] as Module[]
    }
}
