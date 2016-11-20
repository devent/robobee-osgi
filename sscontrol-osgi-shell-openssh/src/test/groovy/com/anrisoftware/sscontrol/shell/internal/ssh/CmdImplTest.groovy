/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-shell-openssh.
 *
 * sscontrol-osgi-shell-openssh is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-shell-openssh is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-shell-openssh. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.shell.internal.ssh

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.joda.time.Duration
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.sscontrol.shell.external.Cmd
import com.anrisoftware.sscontrol.shell.external.utils.CmdUtilsModules
import com.anrisoftware.sscontrol.shell.internal.cmd.CmdModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * 
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class CmdImplTest {

    @Test
    void "test commands with control master"() {
        def defargs = [:]
        defargs.log = log
        defargs.timeout = Duration.standardSeconds(5)
        defargs.sshHost = 'localhost'
        defargs.env = [PATH: './']
        defargs.sudoEnv = [PATH: './']
        defargs.sshControlMaster = 'auto'
        defargs.sshControlPersistDuration = Duration.standardSeconds(10)
        def testCases = [
            [
                name: 'one command',
                args: [debugLevel: 2],
                command: 'chmod +w a.txt',
                commands: ['chmod'],
                expected: [chmod: 'chmod_file_out_expected.txt'],
            ],
            [
                name: 'multiple commands',
                args: [debugLevel: 2],
                command: '''
touch a.txt
chmod +w a.txt
''',
                commands: ['touch', 'chmod'],
                expected: [touch: 'touch_file_out_expected.txt', chmod: 'chmod_file_out_expected.txt'],
            ],
            [
                name: 'no control master',
                args: [sshControlMaster: 'no', sshControlPath: '', debugLevel: 2],
                command: '''
touch a.txt
chmod +w a.txt
''',
                commands: ['touch', 'chmod'],
                expected: [touch: 'touch_file_out_expected.txt', chmod: 'chmod_file_out_expected.txt'],
            ],
        ]
        def cmd = cmdRunCaller
        (0..2).each { runTestCases testCases, defargs, cmd, it }
    }

    @Test
    void "test commands"() {
        def defargs = [:]
        defargs.log = log
        defargs.timeout = Duration.standardSeconds(8)
        defargs.sshHost = 'localhost'
        defargs.env = [PATH: './']
        defargs.sudoEnv = [PATH: './']
        def testCases = [
            [
                name: 'one command',
                args: [debugLevel: 2],
                command: 'chmod +w a.txt',
                commands: ['chmod'],
                expected: [chmod: 'chmod_file_out_expected.txt'],
            ],
            [
                name: 'multiple commands',
                args: [debugLevel: 2],
                command: '''
touch a.txt
chmod +w a.txt
''',
                commands: ['touch', 'chmod'],
                expected: [touch: 'touch_file_out_expected.txt', chmod: 'chmod_file_out_expected.txt'],
            ],
            [
                name: 'no control master',
                args: [sshControlMaster: 'no', sshControlPath: '', debugLevel: 2],
                command: '''
touch a.txt
chmod +w a.txt
''',
                commands: ['touch', 'chmod'],
                expected: [touch: 'touch_file_out_expected.txt', chmod: 'chmod_file_out_expected.txt'],
            ],
        ]
        def cmd = cmdRunCaller
        runTestCases testCases, defargs, cmd
    }

    void runTestCases(List testCases, Map defargs, Cmd cmd, int i=0) {
        testCases.eachWithIndex { Map test, int k ->
            log.info '{}. case: "{}": {}', k, test.name, test
            String command = test.command as String
            Map args = new HashMap(defargs)
            args.putAll test.args
            args.chdir = folder.newFolder String.format('%03d_%03d_%s', i, k, test.name)
            args.sudoChdir = args.chdir
            createEchoCommands args.chdir, ['sudo']
            createEchoCommands args.chdir, test.commands
            cmd args, this, threads, command
            Map testExpected = test.expected
            test.commands.each { String it ->
                assertStringContent fileToString(new File(args.chdir, "${it}.out")), resourceToString(CmdImplTest.class.getResource(testExpected[it] as String))
            }
        }
    }

    static Injector injector

    static Threads threads

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    @Inject
    CmdRunCaller cmdRunCaller

    @Before
    void setupTest() {
        injector.injectMembers(this)
    }

    @BeforeClass
    static void setupInjector() {
        toStringStyle
        this.injector = Guice.createInjector(
                new CmdModule(),
                new SshModule(),
                new CmdUtilsModules())
        this.threads = CmdUtilsModules.getThreads(injector)
    }
}
