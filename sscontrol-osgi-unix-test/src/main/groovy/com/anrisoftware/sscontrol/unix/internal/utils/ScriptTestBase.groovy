package com.anrisoftware.sscontrol.unix.internal.utils

import static com.anrisoftware.sscontrol.unix.internal.utils.UnixTestUtil.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.junit.Rule
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
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.resources.templates.internal.maps.TemplatesDefaultMapsModule
import com.anrisoftware.resources.templates.internal.templates.TemplatesResourcesModule
import com.anrisoftware.resources.templates.internal.worker.STDefaultPropertiesModule
import com.anrisoftware.resources.templates.internal.worker.STWorkerModule
import com.anrisoftware.sscontrol.profile.internal.ProfileModule
import com.anrisoftware.sscontrol.profile.internal.HostServicePropertiesImpl.HostServicePropertiesImplFactory
import com.anrisoftware.sscontrol.services.internal.TargetsModule
import com.anrisoftware.sscontrol.services.internal.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.TargetsImpl.TargetsImplFactory
import com.anrisoftware.sscontrol.types.external.HostPropertiesService
import com.anrisoftware.sscontrol.types.external.HostService
import com.anrisoftware.sscontrol.types.external.HostServiceScript
import com.anrisoftware.sscontrol.types.external.HostServices
import com.anrisoftware.sscontrol.types.external.SshHost
import com.anrisoftware.sscontrol.types.external.TargetsService
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Extend this class to test service scripts.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
abstract class ScriptTestBase {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    Injector injector

    @Inject
    HostServicesImplFactory servicesFactory

    @Inject
    ThreadsTestPropertiesProvider threadsProperties

    @Inject
    PropertiesThreadsFactory threadsFactory

    Threads threads

    @Inject
    Localhost localhost

    void doTest(Map test, int k) {
        log.info '{}. case: {}', k, test
        File dir = folder.newFolder()
        def services = servicesFactory.create()
        putServices services
        services.addService 'ssh', localhost
        Eval.me 'service', services, test.input as String
        def all = services.targets.getHosts('default')
        createDummyCommands dir
        services.getServices().each { String name ->
            List<HostService> service = services.getServices(name)
            service.eachWithIndex { HostService s, int i ->
                if (s.name == serviceName) {
                    List<SshHost> targets = s.targets.size() == 0 ? all : s.targets
                    targets.each { SshHost host ->
                        log.info '{}. {} {} {}', i, name, s, host
                        HostServiceScript script = services.getAvailableScriptService('hostname/debian/8').create(services, s, host, threads)
                        script = setupScript script, dir: dir
                        script.run()
                    }
                }
            }
        }
        Closure expected = test.expected
        expected services: services, dir: dir
    }

    abstract String getServiceName()

    abstract void createDummyCommands(File dir)

    abstract void putServices(HostServices services)

    abstract List getAdditionalModules()

    HostServiceScript setupScript(Map args, def script) {
        def chdir = args.dir as File
        script.chdir = chdir
        script.sudoEnv.PATH = chdir
        script.env.PATH = chdir
        return script
    }

    Injector createInjector() {
        this.injector = Guice.createInjector(
                new TargetsModule(),
                new ProfileModule(),
                new PropertiesUtilsModule(),
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

                    @Override
                    protected void configure() {
                        bind TargetsService to TargetsImplFactory
                        bind(HostPropertiesService).to(HostServicePropertiesImplFactory)
                    }
                })
        this.injector = injector.createChildInjector(additionalModules)
        injector
    }

    Threads createThreads() {
        PropertiesThreads threads = threadsFactory.create();
        threads.setProperties threadsProperties.get()
        threads.setName("script");
        threads
    }
}
