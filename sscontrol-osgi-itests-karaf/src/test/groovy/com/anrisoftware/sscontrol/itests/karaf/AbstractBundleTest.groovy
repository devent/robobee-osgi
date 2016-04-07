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
package com.anrisoftware.sscontrol.itests.karaf;

import static org.apache.commons.lang3.StringUtils.*
import static org.ops4j.pax.exam.CoreOptions.*
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.*
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.karaf.shell.api.console.SessionFactory
import org.junit.Before
import org.ops4j.pax.exam.Configuration
import org.ops4j.pax.exam.Option
import org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel
import org.ops4j.pax.exam.options.MavenUrlReference
import org.ops4j.pax.exam.options.UrlReference
import org.ops4j.pax.exam.util.PathUtils
import org.osgi.framework.BundleContext

@CompileStatic
@Slf4j
class AbstractBundleTest {

    @Inject
    BundleContext bundleContext

    @Inject
    SessionFactory sessionFactory

    UrlReference repo = maven('com.anrisoftware.sscontrol', 'sscontrol-osgi-features', '1.0.0-SNAPSHOT').type('xml')

    MavenUrlReference karafUrl = maven('org.apache.karaf', 'apache-karaf', '4.0.4').type('tar.gz')

    ExecuteCommandKaraf executeCommand

    File getConfigFile(String path) {
        return new File(this.getClass().getResource(path).getFile());
    }

    @Configuration
    Option[] config() {
        createConfig() as Option[]
    }

    List<Option> createConfig() {
        def options = []
        options << junitBundles()
        options << systemProperty('logback.configurationFile').value("file:${PathUtils.baseDir}/src/test/resources/logback-test.xml")
        options << logLevel(LogLevel.INFO)
        options << karafDistributionConfiguration().frameworkUrl(karafUrl).name('Apache Karaf').unpackDirectory('target/exam' as File)
        options << keepRuntimeFolder()
        options << features(repo, 'sscontrol-osgi')
    }

    @Before
    void setupExecuteCommand() {
        this.executeCommand = new ExecuteCommandKaraf(sessionFactory: sessionFactory)
        executeCommand.build()
    }
}
