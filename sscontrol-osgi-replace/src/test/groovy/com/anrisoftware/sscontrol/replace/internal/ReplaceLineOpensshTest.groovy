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
package com.anrisoftware.sscontrol.replace.internal

import static com.anrisoftware.globalpom.utils.TestUtils.assertStringContent
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokensTemplateModule
import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.sscontrol.replace.external.Replace.ReplaceFactory
import com.anrisoftware.sscontrol.replace.internal.ParseSedSyntax.ParseSedSyntaxFactory
import com.anrisoftware.sscontrol.shell.external.utils.AbstractCmdTestBase
import com.anrisoftware.sscontrol.shell.external.utils.CmdUtilsModules
import com.anrisoftware.sscontrol.shell.external.utils.SshFactory
import com.anrisoftware.sscontrol.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.shell.internal.copy.CopyModule
import com.anrisoftware.sscontrol.shell.internal.fetch.FetchModule
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
class ReplaceLineOpensshTest extends AbstractCmdTestBase {

    static Threads threads

    @Inject
    ReplaceFactory replaceFactory

    @Inject
    ParseSedSyntaxFactory parseSedSyntaxFactory

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    static Map expectedResources = [
        line_search_replace_scp: ReplaceLineOpensshTest.class.getResource('line_search_replace_scp_expected.txt'),
        line_sed_replace_scp: ReplaceLineOpensshTest.class.getResource('line_sed_replace_scp_expected.txt'),
    ]

    @Test
    void "replace line"() {
        def testCases = [
            [
                enabled: true,
                name: "line_search_replace",
                args: [
                    dest: "/tmp/aaa.txt",
                ],
                lines: [
                    search: /(?m)^test=.*/,
                    replace: 'test=replaced',
                ],
                expected: { Map args ->
                    File dir = args.dir as File
                    String name = args.name as String
                    assertStringContent fileToString(new File(dir, 'scp.out')), resourceToString(expectedResources["${name}_scp"] as URL)
                },
            ],
            [
                enabled: true,
                name: "line_sed_replace",
                args: [
                    dest: "/tmp/aaa.txt",
                ],
                lines: [
                    replace: 's/(?m)^test=.*/test=replaced/',
                ],
                expected: { Map args ->
                    File dir = args.dir as File
                    String name = args.name as String
                    assertStringContent fileToString(new File(dir, 'scp.out')), resourceToString(expectedResources["${name}_scp"] as URL)
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            if(test.enabled) {
                def tmp = folder.newFolder()
                test.args.tmp = folder.newFile("replace_test.txt")
                FileUtils.write test.args.tmp, 'test=foo\n'
                log.info '{}. case: {}', k, test
                test.host = SshFactory.localhost(injector).hosts[0]
                doTest test, tmp, k
            }
        }
    }

    def createCmd(Map test, File tmp, int k) {
        def replace = replaceFactory.create test.args, test.host, this, threads, log
        replace.line test.lines
        createEchoCommands tmp, [
            'mkdir',
            'chown',
            'chmod',
            'sudo',
            'scp',
        ]
        return replace
    }

    @Before
    void setupTest() {
        super.setupTest()
        this.threads = CmdUtilsModules.getThreads(injector)
    }

    Module[] getAdditionalModules() {
        [
            new TokensTemplateModule(),
            new SshModule(),
            new CmdModule(),
            new ReplaceModule(),
            new ScpModule(),
            new CopyModule(),
            new FetchModule(),
            new CmdUtilsModules(),
        ] as Module[]
    }
}
