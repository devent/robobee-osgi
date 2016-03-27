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
package com.anrisoftware.sscontrol.database.internal

import static org.ops4j.pax.exam.CoreOptions.*
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.junit.Test
import org.junit.runner.RunWith
import org.ops4j.pax.exam.Option
import org.ops4j.pax.exam.junit.PaxExam
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy
import org.ops4j.pax.exam.spi.reactors.PerClass

import com.anrisoftware.sscontrol.database.external.Database

@Slf4j
@CompileStatic
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
class DatabaseBundleTest extends AbstractDatabaseTest {

    @Inject
    Database database

    @Test
    void "load database script"() {
        def script = new DatabaseScript()
        script.setProperty 'database', database
        script.run()
    }

    List<Option> getConfig() {
        paxConfig << mavenBundle('com.anrisoftware.sscontrol', 'sscontrol-osgi-types')
        paxConfig << mavenBundle('com.anrisoftware.sscontrol', 'sscontrol-osgi-database')
    }
}
