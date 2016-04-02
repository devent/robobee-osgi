/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-test.
 *
 * sscontrol-osgi-test is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-test is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-test. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.test.karaf;

import static org.ops4j.pax.exam.CoreOptions.*
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.*
import static org.ops4j.pax.exam.util.PathUtils.*

import javax.inject.Inject

import org.junit.Test
import org.junit.runner.RunWith
import org.ops4j.pax.exam.Configuration
import org.ops4j.pax.exam.Option
import org.ops4j.pax.exam.junit.PaxExam
import org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy
import org.ops4j.pax.exam.spi.reactors.PerMethod
import org.osgi.framework.BundleContext

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
class KarafTest {

    @Inject
    BundleContext bundleContext

    File getConfigFile(String path) {
        return new File(this.getClass().getResource(path).getFile());
    }

    @Configuration
    Option[] config() {
        List config = []
        config << systemProperty('logback.configurationFile').value("file:${baseDir}/src/test/resources/logback-test.xml")
        config << configureConsole().ignoreLocalConsole().startRemoteShell()
        config << logLevel(LogLevel.INFO)
        //config << keepRuntimeFolder()
        //config << debugConfiguration('5005', true)
        def karafUrl = maven('org.apache.karaf', 'apache-karaf', '4.0.4').type('tar.gz')
        def tasklistRepo = maven('com.anrisoftware.sscontrol', 'sscontrol-osgi-app-feature', '1.0-SNAPSHOT').type('xml').classifier('features')
        config << karafDistributionConfiguration().frameworkUrl(karafUrl).name('Apache Karaf').unpackDirectory(new File('target/exam'))
        config << features(tasklistRepo, 'sscontrol-osgi-database')
    }

    @Test
    void test1() {
    }
}
