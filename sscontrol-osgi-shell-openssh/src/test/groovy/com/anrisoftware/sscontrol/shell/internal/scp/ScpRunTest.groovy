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
package com.anrisoftware.sscontrol.shell.internal.scp

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Provider

import org.joda.time.Duration
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.globalpom.threads.properties.external.PropertiesThreads
import com.anrisoftware.globalpom.threads.properties.external.PropertiesThreadsFactory
import com.anrisoftware.sscontrol.shell.internal.cmd.CmdThreadsTestPropertiesProvider
import com.anrisoftware.sscontrol.shell.internal.cmd.UtilsModules;
import com.anrisoftware.sscontrol.shell.internal.scp.ScpRun.ScpRunFactory
import com.anrisoftware.sscontrol.shell.internal.ssh.CmdModule
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * 
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class ScpRunTest {

    @Test
    void "test scp with control master"() {
        def defargs = [:]
        defargs.log = log
        defargs.src = 'src/file.txt'
        defargs.timeout = Duration.standardSeconds(30)
        defargs.env = [PATH: './']
        defargs.sshHost = 'localhost'
        defargs.sshControlMaster = 'auto'
        defargs.sshControlPersistDuration = Duration.standardSeconds(10)
        def testCases = [
            [
                name: 'scp_debug_master',
                args: [debugLevel: 2, dest: 'dest'],
                commands: ['scp'],
                expected: [scp: 'scp_debug_master_out_expected.txt'],
            ],
        ]
        def factory = scpRunFactory
        testCases.eachWithIndex { Map test, int k ->
            runTestCases defargs, test, k, factory
        }
    }

    void runTestCases(Map defargs, Map test, int k, ScpRunFactory scpFactory) {
        log.info '{}. case: {}', k, test
        Map args = new HashMap(defargs)
        args.putAll test.args
        args.chdir = folder.newFolder String.format('%03d_%s', k, test.name)
        createEchoCommands args.chdir, test.commands
        def scp = scpFactory.create args, this, threads
        scp()
        Map testExpected = test.expected
        test.commands.each { String it ->
            assertStringContent fileToString(toFile(args, it)), resourceToString(ScpRunTest.class.getResource(testExpected[it] as String))
        }
    }

    File toFile(Map args, String name) {
        def file = new File(args.chdir, "${name}.out")
        assert file != null
        return file
    }

    static Injector injector

    static Threads threads

    static PropertiesThreadsFactory threadsFactory

    static Provider<? extends Properties> threadsProperties

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    @Inject
    ScpRunFactory scpRunFactory

    @Before
    void setupTest() {
        injector.injectMembers(this)
    }

    @BeforeClass
    static void setupInjector() {
        toStringStyle
        this.injector = Guice.createInjector(
                new CmdModule(),
                new ScpModule(),
                new UtilsModules(),
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
