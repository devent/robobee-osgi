/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-unix.
 *
 * sscontrol-osgi-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-unix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.unix.internal.core

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.unix.internal.utils.UnixTestUtil.*
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Provider

import org.joda.time.Duration
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.durationformat.DurationFormatModule
import com.anrisoftware.globalpom.durationsimpleformat.DurationSimpleFormatModule
import com.anrisoftware.globalpom.exec.internal.command.DefaultCommandLineModule
import com.anrisoftware.globalpom.exec.internal.core.DefaultProcessModule
import com.anrisoftware.globalpom.exec.internal.logoutputs.LogOutputsModule
import com.anrisoftware.globalpom.exec.internal.pipeoutputs.PipeOutputsModule
import com.anrisoftware.globalpom.exec.internal.runcommands.RunCommandsModule
import com.anrisoftware.globalpom.exec.internal.script.ScriptCommandModule
import com.anrisoftware.globalpom.exec.internal.scriptprocess.ScriptProcessModule
import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.globalpom.threads.properties.external.PropertiesThreads
import com.anrisoftware.globalpom.threads.properties.external.PropertiesThreadsFactory
import com.anrisoftware.globalpom.threads.properties.internal.PropertiesThreadsModule
import com.anrisoftware.resources.templates.internal.maps.TemplatesDefaultMapsModule
import com.anrisoftware.resources.templates.internal.templates.TemplatesResourcesModule
import com.anrisoftware.resources.templates.internal.worker.STDefaultPropertiesModule
import com.anrisoftware.resources.templates.internal.worker.STWorkerModule
import com.anrisoftware.sscontrol.cmd.external.Cmd
import com.anrisoftware.sscontrol.cmd.internal.core.CmdModule
import com.anrisoftware.sscontrol.cmd.internal.core.CmdRunCaller
import com.google.inject.AbstractModule
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
        (0..20).each { runTestCases testCases, defargs, cmd, it }
    }

    @Test
    void "test commands"() {
        def defargs = [:]
        defargs.log = log
        defargs.timeout = Duration.standardSeconds(8)
        defargs.sshHost = 'localhost'
        defargs.env = [PATH: './']
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

    static PropertiesThreadsFactory threadsFactory

    static Provider<? extends Properties> threadsProperties

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
                new RunCommandsModule(),
                new LogOutputsModule(),
                new PipeOutputsModule(),
                new DefaultProcessModule(),
                new DefaultCommandLineModule(),
                new ScriptCommandModule(),
                new ScriptProcessModule(),
                new STDefaultPropertiesModule(),
                new STWorkerModule(),
                new TemplatesDefaultMapsModule(),
                new TemplatesResourcesModule(),
                new PropertiesThreadsModule(),
                new DurationSimpleFormatModule(),
                new DurationFormatModule(),
                new AbstractModule() {
                    protected void configure() {
                    }
                })
        this.threadsProperties = injector.getInstance CmdThreadsTestPropertiesProvider
        this.threadsFactory = injector.getInstance PropertiesThreadsFactory
        this.threads = createThreads()
    }

    static Threads createThreads() {
        PropertiesThreads threads = threadsFactory.create();
        threads.setProperties threadsProperties.get()
        threads.setName("script");
        threads
    }
}
