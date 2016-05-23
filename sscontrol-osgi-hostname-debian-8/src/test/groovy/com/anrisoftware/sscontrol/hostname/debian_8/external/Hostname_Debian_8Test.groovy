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
package com.anrisoftware.sscontrol.hostname.debian_8.external

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.util.logging.Slf4j

import javax.inject.Provider

import org.apache.sling.testing.mock.osgi.junit.OsgiContext
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test

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
import com.anrisoftware.sscontrol.hostname.debian_8.external.Hostname_Debian_8;
import com.anrisoftware.sscontrol.unix.internal.core.CmdImpl
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see PropertiesThreadsServiceImpl
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 3.1
 */
@Slf4j
class Hostname_Debian_8Test {

    @Test
    void "hostname service"() {
        def hostname = context.getService Hostname_Debian_8
        hostname.threads = threads
        hostname.run()
        println hostname
    }

    @ClassRule
    public static final OsgiContext context = new OsgiContext()

    static Injector injector

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
        context.registerInjectActivateService(new Hostname_Debian_8(), null)
        this.threadsService = context.registerInjectActivateService(new PropertiesThreadsServiceImpl(), null)
        this.injector = Guice.createInjector()
        this.threadsProperties = injector.getInstance ThreadsTestPropertiesProvider
        setupThreads()
    }

    static void setupThreads() {
        PropertiesThreads threads = threadsService.create();
        threads.setProperties threadsProperties.get()
        threads.setName("script");
        this.threads = threads
    }
}
