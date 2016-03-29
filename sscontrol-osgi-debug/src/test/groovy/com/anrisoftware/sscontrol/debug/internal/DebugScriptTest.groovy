/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-debug.
 *
 * sscontrol-osgi-debug is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-debug is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-debug. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.debug.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.debug.external.DebugLogging
import com.anrisoftware.sscontrol.types.internal.TypesModule
import com.google.inject.Guice

@Slf4j
@CompileStatic
class DebugScriptTest {

    @Inject
    DebugLogging debug

    @Test
    void "load database script"() {
        def script = new DebugScript()
        script.setProperty 'debugParent', debug
        script.run()
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(new DebugModule(), new TypesModule()).injectMembers(this)
    }
}
