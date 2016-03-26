/*
 * Copyright 2011-2016 Erwin Müller <erwin.mueller@deventm.org>
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

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.database.external.Database
import com.anrisoftware.sscontrol.types.internal.TypesModule
import com.google.inject.Guice

@Slf4j
@CompileStatic
class DatabaseScriptTest {

    @Inject
    Database database

    @Test
    void "load database script"() {
        def script = new DatabaseScript()
        script.setProperty 'database', database
        script.run()
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(new DatabaseModule(), new TypesModule()).injectMembers(this)
    }
}
