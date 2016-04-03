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
package com.anrisoftware.sscontrol.itests.karaf;

import static org.ops4j.pax.exam.CoreOptions.*
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.*

import javax.inject.Inject

import org.junit.Test
import org.junit.runner.RunWith
import org.ops4j.pax.exam.Configuration
import org.ops4j.pax.exam.Option
import org.ops4j.pax.exam.junit.PaxExam
import org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy
import org.ops4j.pax.exam.spi.reactors.PerSuite
import org.ops4j.pax.exam.util.PathUtils
import org.osgi.framework.BundleContext

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
class KarafTest {

    @Inject
    BundleContext bundleContext

    File getConfigFile(String path) {
        return new File(this.getClass().getResource(path).getFile());
    }

    @Configuration
    Option[] config() {
        def options = []
        def karafUrl = maven('org.apache.karaf', 'apache-karaf', '4.0.4').type('tar.gz')
        options << junitBundles()
        options << systemProperty('logback.configurationFile').value("file:${PathUtils.baseDir}/src/test/resources/logback-test.xml")
        options << logLevel(LogLevel.INFO)
        def repo = maven('com.anrisoftware.sscontrol', 'sscontrol-osgi-features').versionAsInProject().type('xml')
        options << karafDistributionConfiguration().frameworkUrl(karafUrl).name('Apache Karaf').unpackDirectory('target/exam' as File)
        //options << keepRuntimeFolder()
        options << features(repo, 'sscontrol-osgi')
    }

    @Test
    void test1() throws Exception {
    }
}
