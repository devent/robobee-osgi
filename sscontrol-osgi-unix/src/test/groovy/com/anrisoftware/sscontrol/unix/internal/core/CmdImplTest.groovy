package com.anrisoftware.sscontrol.unix.internal.core

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.unix.internal.utils.UnixTestUtil.*
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Provider

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
    void "test commands"() {
        def testCases = [
            [
                name: 'chmod files',
                args: [log: log, sshHost: 'localhost', env: [PATH: './']],
                command: 'chmod +w a.txt',
                commands: ['chmod'],
                expected: [chmod: 'chmod_file_out_expected.txt'],
            ],
            [
                name: 'chmod files',
                args: [log: log, sshHost: 'localhost', env: [PATH: './']],
                command: '''
touch a.txt
chmod +w a.txt
''',
                commands: ['touch', 'chmod'],
                expected: [touch: 'touch_file_out_expected.txt', chmod: 'chmod_file_out_expected.txt'],
            ],
        ]
        def cmd = cmdRunCaller
        testCases.eachWithIndex { Map test, int k ->
            log.info '{}. case: "{}": {}', k, test.name, test
            String command = test.command as String
            def dir = folder.newFolder String.format('%03d_%s', k, test.name)
            test.args.chdir = dir
            createEchoCommands dir, test.commands
            cmd test.args, this, threads, command
            Map testExpected = test.expected
            test.commands.each { String it ->
                assertStringContent fileToString(new File(dir, "${it}.out")), resourceToString(CmdImplTest.class.getResource(testExpected[it] as String))
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
