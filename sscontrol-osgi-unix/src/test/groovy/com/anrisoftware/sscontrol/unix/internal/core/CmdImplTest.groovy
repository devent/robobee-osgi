package com.anrisoftware.sscontrol.unix.internal.core

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.unix.internal.utils.UnixTestUtil.*
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Provider

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.exec.internal.command.DefaultCommandLineModule
import com.anrisoftware.globalpom.exec.internal.core.DefaultProcessModule
import com.anrisoftware.globalpom.exec.internal.logoutputs.LogOutputsModule
import com.anrisoftware.globalpom.exec.internal.pipeoutputs.PipeOutputsModule
import com.anrisoftware.globalpom.exec.internal.runcommands.RunCommandsModule
import com.anrisoftware.globalpom.exec.internal.script.ScriptCommandModule
import com.anrisoftware.globalpom.exec.internal.scriptprocess.ScriptProcessModule
import com.anrisoftware.globalpom.strings.StringsModule
import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.globalpom.threads.properties.external.PropertiesThreads
import com.anrisoftware.globalpom.threads.properties.external.PropertiesThreadsFactory
import com.anrisoftware.globalpom.threads.properties.internal.PropertiesThreadsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.resources.templates.internal.maps.TemplatesDefaultMapsModule
import com.anrisoftware.resources.templates.internal.templates.TemplatesResourcesModule
import com.anrisoftware.resources.templates.internal.worker.STDefaultPropertiesModule
import com.anrisoftware.resources.templates.internal.worker.STWorkerModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.profile.internal.ProfileImpl
import com.anrisoftware.sscontrol.profile.internal.ProfileModule
import com.anrisoftware.sscontrol.profile.internal.ProfilePropertiesModule
import com.anrisoftware.sscontrol.profile.internal.ProfileImpl.ProfileImplFactory
import com.anrisoftware.sscontrol.profile.internal.ProfilePropertiesImpl.ProfilePropertiesImplFactory
import com.anrisoftware.sscontrol.scripts.internal.ScriptsRepositoryModule
import com.anrisoftware.sscontrol.scripts.internal.ScriptsRepositoryImpl.ScriptsRepositoryImplFactory
import com.anrisoftware.sscontrol.ssh.internal.SshModule
import com.anrisoftware.sscontrol.ssh.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.types.external.Profile
import com.anrisoftware.sscontrol.types.external.ProfilePropertiesService
import com.anrisoftware.sscontrol.types.external.SscontrolScript
import com.anrisoftware.sscontrol.types.external.SscontrolServiceScript
import com.anrisoftware.sscontrol.types.internal.TypesModule
import com.google.inject.AbstractModule
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
        Profile profile = Eval.me('profile', profileFactory.create(), '''
profile 'Ubuntu 12.04' with {
    hostname
}
profile
''') as Profile
        SscontrolScript ssh = Eval.me('ssh', sshFactory.create(), '''
''') as SscontrolScript
        def cmd = cmdRunCaller
        cmd 'chmod', [
            getLog: { log },
            getThreads: { threads },
            getProfile: { profile.getEntry('hostname') },
            getDefaultProperties: { properties },
            getScriptsRepository: {
                def repository = scriptsRepositoryFactory.create()
                repository.putScript 'ssh', ssh
                repository
            },
        ] as SscontrolServiceScript, files: [], mod: '+w'
    }

    @Test
    void "run command ssh"() {
        def dir = folder.newFolder()
        String command = 'chmod'
        def echo = createEchoCommand dir, command
        def properties = cmdProperties.get()
        properties.put "${command}_command", echo
        ProfileImpl profile = Eval.me('profile', profileFactory.create(), '''
profile 'Ubuntu 12.04' with {
    hostname
}
profile
''') as ProfileImpl
        def cmd = cmdRunCaller
        cmd 'chmod', [
            getLog: { log },
            getThreads: { threads },
            getProfile: { profile.getEntry('hostname') },
            getDefaultProperties: { properties }
        ] as SscontrolServiceScript, files: [], mod: '+w'
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
        Profile profile = Eval.me('profile', profileFactory.create(), '''
profile 'Ubuntu 12.04' with {
    hostname
}
profile
''') as Profile
        def cmd = cmdRunCaller
        testCases.eachWithIndex { Map test, int k ->
            log.info '{}. case: "{}": {}', k, test.name, test
            String command = test.command as String
            def dir = folder.newFolder "${command}_$k"
            def echo = createEchoCommand dir, command
            def properties = cmdProperties.get()
            properties.put "${command}_command", echo
            def args = test.args as Map<String, Object>
            cmd args, command, [
                getLog: { log },
                getThreads: { threads },
                getProfile: { profile.getEntry('hostname') },
                getDefaultProperties: { properties }
            ] as SscontrolServiceScript
            Map testExpected = test.expected
            assertStringContent fileToString(new File(dir, "${command}.out")), resourceToString(CmdImplTest.class.getResource(testExpected.output as String))
        }
    }

    static Injector injector

    static Threads threads

    static PropertiesThreadsFactory threadsFactory

    static Provider<? extends Properties> cmdProperties

    static Provider<? extends Properties> threadsProperties

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    @Inject
    CmdRunCaller cmdRunCaller

    @Inject
    ProfileImplFactory profileFactory

    @Inject
    ScriptsRepositoryImplFactory scriptsRepositoryFactory

    @Inject
    SshImplFactory sshFactory

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
                new SshModule(),
                new ScriptsRepositoryModule(),
                new PropertiesThreadsModule(),
                new ProfileModule(),
                new ProfilePropertiesModule(),
                new DebugLoggingModule(),
                new TypesModule(),
                new StringsModule(),
                new PropertiesUtilsModule(),
                new AbstractModule() {
                    protected void configure() {
                        bind ProfilePropertiesService to ProfilePropertiesImplFactory
                    }
                })
        this.cmdProperties = injector.getInstance CmdTestPropertiesProvider
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
