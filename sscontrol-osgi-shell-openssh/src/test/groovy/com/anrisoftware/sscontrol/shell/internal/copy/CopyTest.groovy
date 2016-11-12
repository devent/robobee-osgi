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
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.sscontrol.copy.external.Copy.CopyFactory
import com.anrisoftware.sscontrol.shell.external.utils.PartScriptTestBase
import com.anrisoftware.sscontrol.shell.external.utils.SshFactory
import com.anrisoftware.sscontrol.shell.internal.cmd.UtilsModules
import com.anrisoftware.sscontrol.shell.internal.scp.ScpModule
import com.anrisoftware.sscontrol.shell.internal.ssh.CmdModule
import com.google.inject.Injector
import com.google.inject.Module

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class CopyTest extends PartScriptTestBase {

    static Threads threads

    @Inject
    CopyFactory copyFactory

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    static Map expectedResources = [
        copy_src_dest: CopyTest.class.getResource('copy_src_dest_expected.txt'),
    ]

    @Test
    void "copy cases"() {
        def testCases = [
            [
                name: "copy_src_dest",
                args: [
                    src: "aaa.txt",
                    dest: "/tmp",
                ],
                expected: { Map args ->
                    File dir = args.dir as File
                    String name = args.name as String
                    assertStringContent fileToString(new File(dir, 'scp.out')), resourceToString(expectedResources[name] as URL)
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            log.info '{}. case: {}', k, test
            def tmp = folder.newFolder()
            test.host = SshFactory.localhost(injector).hosts[0]
            doTest test, tmp, k
        }
    }

    def createCmd(Map test, File tmp, int k) {
        def fetch = copyFactory.create test.args, test.host, this, threads, log
        createEchoCommands tmp, ['scp']
        return fetch
    }

    @Before
    void setupTest() {
        super.setupTest()
        this.threads = UtilsModules.getThreads(injector)
    }

    Module[] getAdditionalModules() {
        [
            new CmdModule(),
            new CopyModule(),
            new ScpModule(),
            new UtilsModules(),
        ] as Module[]
    }
}
