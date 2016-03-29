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
package com.anrisoftware.sscontrol.test.bundles;

import static org.ops4j.pax.exam.CoreOptions.*
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.ops4j.pax.exam.Configuration
import org.ops4j.pax.exam.Option
import org.ops4j.pax.exam.util.PathUtils
import org.osgi.framework.Bundle
import org.osgi.framework.BundleContext

/**
 * Configures Pax Exam for a bundle test.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
@CompileStatic
abstract class AbstractBundlesTest {

    @Inject
    BundleContext bc

    @Configuration
    Option[] config() {
        paxConfig as Option[]
    }

    List getPaxConfig() {
        def list = []
        list << junitBundles()
        list << mavenBundle('org.slf4j', 'slf4j-api', '1.7.13')
        list << mavenBundle('ch.qos.logback', 'logback-core', '1.0.6')
        list << mavenBundle('ch.qos.logback', 'logback-classic', '1.0.6')
        list << mavenBundle('com.google.guava', 'guava', '19.0')
        list << mavenBundle('com.google.inject', 'guice', '4.0')
        list << mavenBundle('com.google.inject.extensions', 'guice-assistedinject', '4.0').noStart()
        list << mavenBundle('org.apache.commons', 'commons-lang3', '3.4')
        list << mavenBundle('org.apache.servicemix.bundles', 'org.apache.servicemix.bundles.aopalliance', '1.0_4')
        list << mavenBundle('org.apache.servicemix.bundles', 'org.apache.servicemix.bundles.asm', '3.3.1_1')
        list << mavenBundle('org.apache.servicemix.bundles', 'org.apache.servicemix.bundles.javax-inject', '1_2')
        list << mavenBundle('org.codehaus.groovy', 'groovy', '2.4.5')
        list << mavenBundle('com.anrisoftware.sscontrol.osgi', 'com.anrisoftware.globalpom.log')
        list << systemProperty('logback.configurationFile').value("file:${PathUtils.baseDir}/src/test/resources/logback-test.xml")
        list << mavenBundle('com.anrisoftware.sscontrol', 'sscontrol-osgi-types')
    }

    List getExpectedActiveBundles() {
        List list = []
        list << 'com.anrisoftware.sscontrol.types'
    }

    static assertAllBundlesActive(BundleContext context, def expectedActiveBundles) {
        assert context != null
        List<Bundle> bundles = context.bundles as List
        expectedActiveBundles.inject([]) { List list, String name ->
            def found = bundles.find { Bundle bundle -> bundle.symbolicName == name }
            log.info 'Found for \'{}\' bundle \'{}\'', name, found
            list << found
        }.each { Bundle bundle ->
            log.info 'Check bundle {}', bundle
            assert bundle.state == Bundle.ACTIVE: "Bundle ${bundle.symbolicName} in state ${bundle.state}"
        }
    }
}
