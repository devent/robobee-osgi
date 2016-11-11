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
package com.anrisoftware.sscontrol.groovy.script.external

import static com.anrisoftware.globalpom.utils.TestUtils.*

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
import com.anrisoftware.sscontrol.shell.external.Cmd
import com.anrisoftware.sscontrol.shell.external.Shell.ShellFactory
import com.anrisoftware.sscontrol.shell.internal.CmdImpl
import com.anrisoftware.sscontrol.shell.internal.CmdModule
import com.anrisoftware.sscontrol.shell.internal.CmdRunCaller
import com.anrisoftware.sscontrol.shell.internal.ShellModule
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class ScriptBaseTest {

    @Test
    void "shell"() {
        def testCases = [
            [
                name: 'shell basic',
                input: new ScriptBase() {

                    @Override
                    Properties getDefaultProperties() {
                    }

                    @Override
                    def run() {
                        shell "echo 'test shell'" call()
                    }

                    @Override
                    String getSystemName() {
                        ''
                    }

                    @Override
                    String getSystemVersion() {
                        ''
                    }
                },
                expected: {
                }
            ],
            [
                name: 'shell env x=y',
                input: new ScriptBase() {

                    @Override
                    Properties getDefaultProperties() {
                    }

                    @Override
                    @CompileDynamic
                    def run() {
                        shell "echo \"test shell \$STRING\"" with { //
                            env "STRING=hello" } call()
                    }

                    @Override
                    String getSystemName() {
                        ''
                    }

                    @Override
                    String getSystemVersion() {
                        ''
                    }
                },
                expected: {
                }
            ],
            [
                name: 'shell env single quote',
                input: new ScriptBase() {

                    @Override
                    Properties getDefaultProperties() {
                    }

                    @Override
                    @CompileDynamic
                    def run() {
                        shell "echo \"test shell \$STRING\"" with { //
                            env "STRING='hello'" } call()
                    }

                    @Override
                    String getSystemName() {
                        ''
                    }

                    @Override
                    String getSystemVersion() {
                        ''
                    }
                },
                expected: {
                }
            ],
            [
                name: 'shell env var expansion',
                input: new ScriptBase() {

                    @Override
                    Properties getDefaultProperties() {
                    }

                    @Override
                    @CompileDynamic
                    def run() {
                        shell "echo \"test shell \$STRING\"" with { //
                            env "STRING=\"hello \$HOSTNAME\"" } call()
                    }

                    @Override
                    String getSystemName() {
                        ''
                    }

                    @Override
                    String getSystemVersion() {
                        ''
                    }
                },
                expected: {
                }
            ],
            [
                name: 'shell env args expansion',
                input: new ScriptBase() {

                    @Override
                    Properties getDefaultProperties() {
                    }

                    @Override
                    @CompileDynamic
                    def run() {
                        shell "echo \"test shell \$STRING\"" with {
                            env name: "STRING", value: "hello world"
                        } call()
                    }

                    @Override
                    String getSystemName() {
                        ''
                    }

                    @Override
                    String getSystemVersion() {
                        ''
                    }
                },
                expected: {
                }
            ],
            [
                name: 'shell env args no expansion',
                input: new ScriptBase() {

                    @Override
                    Properties getDefaultProperties() {
                    }

                    @Override
                    @CompileDynamic
                    def run() {
                        shell "echo \"test shell \$STRING\"" with {
                            env name: "STRING", value: "hello \$HOSTNAME", literally: false
                        } call()
                    }

                    @Override
                    String getSystemName() {
                        ''
                    }

                    @Override
                    String getSystemVersion() {
                        ''
                    }
                },
                expected: {
                }
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            log.info '{}. --- {} --- case: {}', k, test.name, test
            def script = createScript(test)
            script.run()
            Closure expected = test.expected
            expected()
        }
    }

    @CompileStatic
    ScriptBase createScript(Map test) {
        ScriptBase script = test.input as ScriptBase
        script.target = localhost
        script.shell = shell
        script.threads = threads
        return script
    }

    @Inject
    ShellFactory shell

    static Injector injector

    static Threads threads

    static PropertiesThreadsFactory threadsFactory

    static Provider<? extends Properties> threadsProperties

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    @Inject
    CmdRunCaller cmdRunCaller

    @Inject
    Localhost localhost

    @Before
    void setupTest() {
        injector.injectMembers(this)
    }

    @BeforeClass
    static void setupInjector() {
        toStringStyle
        this.injector = Guice.createInjector(
                new ShellModule(),
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
                        bind Cmd to CmdImpl
                    }
                })
        this.threadsProperties = injector.getInstance ThreadsTestPropertiesProvider
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
