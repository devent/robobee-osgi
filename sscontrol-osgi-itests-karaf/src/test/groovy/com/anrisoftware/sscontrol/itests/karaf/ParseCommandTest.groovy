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

import static org.apache.commons.lang3.StringUtils.*
import static org.ops4j.pax.exam.CoreOptions.*
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.*
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.ops4j.pax.exam.Option
import org.ops4j.pax.exam.junit.PaxExam
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy
import org.ops4j.pax.exam.spi.reactors.PerClass
import org.ops4j.pax.exam.util.PathUtils

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
@CompileStatic
@Slf4j
class ParseCommandTest extends AbstractBundleTest {

    List<Option> createConfig() {
        def options = super.createConfig()
        options << features(repo, 'sscontrol-osgi-groovy-parser')
        options << features(repo, 'sscontrol-osgi-command-parse')
        options << features(repo, 'sscontrol-osgi-dhclient')
    }

    @Test
    void "parse command dhclient"() {
        def result = executeCommand.executeCommand "sscontrol:parse '${dhclientScript}'"
        log.info 'Result: {}', result
    }

    static final URI dhclientScript = new File("${PathUtils.baseDir}/../../../../test/DhclientScript.groovy").toURI()

    @BeforeClass
    static void loadResources() {
        assert new File(dhclientScript).isFile()
    }
}
