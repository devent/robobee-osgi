package com.anrisoftware.sscontrol.shell.external.utils

import javax.inject.Provider

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
import com.anrisoftware.globalpom.threads.properties.external.PropertiesThreadsFactory
import com.anrisoftware.globalpom.threads.properties.internal.PropertiesThreadsModule
import com.anrisoftware.resources.templates.internal.maps.TemplatesDefaultMapsModule
import com.anrisoftware.resources.templates.internal.templates.TemplatesResourcesModule
import com.anrisoftware.resources.templates.internal.worker.STDefaultPropertiesModule
import com.anrisoftware.resources.templates.internal.worker.STWorkerModule
import com.google.inject.AbstractModule
import com.google.inject.Injector

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class CmdUtilsModules extends AbstractModule {

    static Threads threads

    static PropertiesThreadsFactory threadsFactory

    static Provider<? extends Properties> threadsProperties

    static Threads getThreads(Injector injector) {
        def threads = this.threads
        if (threads == null) {
            this.threadsProperties = injector.getInstance ThreadsTestPropertiesProvider
            this.threadsFactory = injector.getInstance PropertiesThreadsFactory
            threads = threadsFactory.create();
            threads.setProperties threadsProperties.get()
            threads.setName("script");
        }
        threads
    }

    @Override
    protected void configure() {
        install(new RunCommandsModule());
        install(new LogOutputsModule());
        install(new PipeOutputsModule());
        install(new DefaultProcessModule());
        install(new DefaultCommandLineModule());
        install(new ScriptCommandModule());
        install(new ScriptProcessModule());
        install(new STDefaultPropertiesModule());
        install(new STWorkerModule());
        install(new TemplatesDefaultMapsModule());
        install(new TemplatesResourcesModule());
        install(new PropertiesThreadsModule());
        install(new DurationSimpleFormatModule());
        install(new DurationFormatModule());
    }
}
