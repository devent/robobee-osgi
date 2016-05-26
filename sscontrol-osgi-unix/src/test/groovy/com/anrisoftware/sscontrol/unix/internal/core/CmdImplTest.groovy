package com.anrisoftware.sscontrol.unix.internal.core

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.unix.internal.utils.UnixTestUtil.*
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Provider

import org.apache.sling.testing.mock.osgi.junit.OsgiContext
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.exec.internal.command.DefaultCommandLineServiceImpl
import com.anrisoftware.globalpom.exec.internal.core.DefaultCommandExecServiceImpl
import com.anrisoftware.globalpom.exec.internal.logoutputs.DebugLogCommandOutputServiceImpl
import com.anrisoftware.globalpom.exec.internal.logoutputs.ErrorLogCommandOutputServiceImpl
import com.anrisoftware.globalpom.exec.internal.pipeoutputs.PipeCommandInputServiceImpl
import com.anrisoftware.globalpom.exec.internal.pipeoutputs.PipeCommandOutputServiceImpl
import com.anrisoftware.globalpom.exec.internal.runcommands.RunCommandsServiceImpl
import com.anrisoftware.globalpom.exec.internal.script.ScriptCommandExecServiceImpl
import com.anrisoftware.globalpom.exec.internal.script.ScriptCommandLineServiceImpl
import com.anrisoftware.globalpom.exec.internal.scriptprocess.ScriptExecServiceImpl
import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.globalpom.threads.properties.external.PropertiesThreads
import com.anrisoftware.globalpom.threads.properties.external.PropertiesThreadsService
import com.anrisoftware.globalpom.threads.properties.internal.PropertiesThreadsServiceImpl
import com.anrisoftware.propertiesutils.ContextPropertiesService
import com.anrisoftware.resources.templates.internal.maps.TemplatesBundlesDefaultMapServiceImpl
import com.anrisoftware.resources.templates.internal.maps.TemplatesDefaultMapServiceImpl
import com.anrisoftware.resources.templates.internal.templates.TemplatesServiceImpl
import com.anrisoftware.resources.templates.internal.worker.STDefaultPropertiesServiceImpl
import com.anrisoftware.resources.templates.internal.worker.STTemplateWorkerServiceImpl
import com.anrisoftware.sscontrol.types.external.SscontrolServiceScript
import com.anrisoftware.sscontrol.unix.external.core.Cmd
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * 
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@CompileStatic
class CmdImplTest {

    @Test
    void "run command"() {
        def dir = folder.newFolder()
        String command = 'chmod'
        def echo = createEchoCommand dir, command
        def properties = cmdProperties.get()
        properties.put "${command}_command", echo
        def cmd = context.getService Cmd
        cmd 'chmod', [getLog: { log }, getThreads: { threads }, getDefaultProperties: { properties }] as SscontrolServiceScript, files: [], mod: '+w'
    }

    @Test
    void "test commands"() {
        def testCases = [
            [
                name: 'chmod file',
                command: 'chmod',
                args: [files: ['a.txt'], mod: '+w'],
                expected: [output: 'chmod_out_expected.txt'],
            ],
            [
                name: 'chmod file recursive',
                command: 'chmod',
                args: [files: ['a.txt'], mod: '+w', recursive: true],
                expected: [output: 'chmod_recursive_out_expected.txt'],
            ],
            [
                name: 'chmod file ssh',
                command: 'chmod',
                args: [files: ['a.txt'], mod: '+w', useSsh: true],
                expected: [output: 'chmod_out_expected.txt'],
            ],
            [
                name: 'chown file',
                command: 'chown',
                args: [files: ['a.txt'], owner: 'foo', ownerGroup: 'foo'],
                expected: [output: 'chown_out_expected.txt'],
            ],
            [
                name: 'chown file recursive',
                command: 'chown',
                args: [files: ['a.txt'], owner: 'foo', ownerGroup: 'foo', recursive: true],
                expected: [output: 'chown_recursive_out_expected.txt'],
            ],
            [
                name: 'install package',
                command: 'install',
                args: [system: 'ubuntu', packages: ['aaa']],
                expected: [output: 'install_out_expected.txt'],
            ],
        ]
        def cmd = context.getService Cmd
        testCases.eachWithIndex { Map test, int k ->
            log.info '{}. case: "{}": {}', k, test.name, test
            String command = test.command as String
            def dir = folder.newFolder "${command}_$k"
            def echo = createEchoCommand dir, command
            def properties = cmdProperties.get()
            properties.put "${command}_command", echo
            def args = test.args as Map<String, Object>
            cmd args, command, [getLog: { log }, getThreads: { threads }, getDefaultProperties: { properties }] as SscontrolServiceScript
            Map testExpected = test.expected
            assertStringContent fileToString(new File(dir, "${command}.out")), resourceToString(CmdImplTest.class.getResource(testExpected.output as String))
        }
    }

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    @ClassRule
    public static final OsgiContext context = new OsgiContext()

    static Injector injector

    static Provider<? extends Properties> cmdProperties

    static Provider<? extends Properties> threadsProperties

    static PropertiesThreadsService threadsService

    static Threads threads

    @BeforeClass
    static void createServices() {
        toStringStyle
        context.registerInjectActivateService(new RunCommandsServiceImpl(), null)
        context.registerInjectActivateService(new ErrorLogCommandOutputServiceImpl(), null)
        context.registerInjectActivateService(new DebugLogCommandOutputServiceImpl(), null)
        context.registerInjectActivateService(new PipeCommandInputServiceImpl(), null)
        context.registerInjectActivateService(new PipeCommandOutputServiceImpl(), null)
        context.registerInjectActivateService(new DefaultCommandExecServiceImpl(), null)
        context.registerInjectActivateService(new DefaultCommandLineServiceImpl(), null)
        context.registerInjectActivateService(new ScriptCommandLineServiceImpl(), null)
        context.registerInjectActivateService(new ScriptCommandExecServiceImpl(), null)
        context.registerInjectActivateService(new ScriptExecServiceImpl(), null)
        context.registerInjectActivateService(new STDefaultPropertiesServiceImpl(), null)
        context.registerInjectActivateService(new STTemplateWorkerServiceImpl(), null)
        context.registerInjectActivateService(new TemplatesBundlesDefaultMapServiceImpl(), null)
        context.registerInjectActivateService(new TemplatesDefaultMapServiceImpl(), null)
        context.registerInjectActivateService(new TemplatesServiceImpl(), null)
        context.registerInjectActivateService(new ContextPropertiesService(), null)
        context.registerInjectActivateService(new CmdImpl(), null)
        this.threadsService = context.registerInjectActivateService(new PropertiesThreadsServiceImpl(), null)
        this.injector = Guice.createInjector()
        this.cmdProperties = injector.getInstance CmdTestPropertiesProvider
        this.threadsProperties = injector.getInstance CmdThreadsTestPropertiesProvider
        setupThreads()
    }

    static void setupThreads() {
        PropertiesThreads threads = threadsService.create();
        threads.setProperties threadsProperties.get()
        threads.setName("script");
        this.threads = threads
    }
}
