/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-database.
 *
 * sscontrol-osgi-database is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-database is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-database. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.internal;

import static org.ops4j.pax.exam.CoreOptions.*
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.ops4j.pax.exam.Configuration
import org.ops4j.pax.exam.Option
import org.ops4j.pax.exam.util.PathUtils
import org.osgi.framework.Bundle
import org.osgi.framework.BundleContext

@Slf4j
@CompileStatic
class AbstractDatabaseTest {

    @Inject
    BundleContext bc

    @Configuration
    Option[] config() {
        getConfig() as Option[]
    }

    static List paxConfig = [
        junitBundles(),
        mavenBundle("org.slf4j", "slf4j-api", "1.7.13"),
        mavenBundle("ch.qos.logback", "logback-core", "1.0.6"),
        mavenBundle("ch.qos.logback", "logback-classic", "1.0.6"),
        mavenBundle("com.google.guava", "guava", "19.0"),
        mavenBundle("com.google.inject", "guice", "4.0"),
        mavenBundle("com.google.inject.extensions", "guice-assistedinject", "4.0").noStart(),
        mavenBundle("org.apache.commons", "commons-lang3", "3.4"),
        mavenBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.aopalliance", "1.0_4"),
        mavenBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.asm", "3.3.1_1"),
        mavenBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.javax-inject", "1_2"),
        mavenBundle("org.codehaus.groovy", "groovy", "2.4.5"),
        mavenBundle('com.anrisoftware.sscontrol.osgi', 'com.anrisoftware.globalpom.log'),
        systemProperty("logback.configurationFile").value("file:${PathUtils.baseDir}/src/test/resources/logback-test.xml")
    ]

    List<Option> getConfig() {
        paxConfig
    }

    static assertAllBundlesActive(BundleContext context) {
        assert context != null
        context.bundles.each { Bundle bundle ->
            log.info 'Check bundle {}', bundle
            assert bundle.state == Bundle.ACTIVE: "Bundle ${bundle.symbolicName} " +
            "in state ${bundle.state}"
        }
    }
}
