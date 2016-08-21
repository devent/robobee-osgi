/*
 * Copyright 2014-2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of globalpomutils-exec.
 *
 * globalpomutils-exec is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * globalpomutils-exec is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with globalpomutils-exec. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.groovy.script.external;

import groovy.transform.CompileStatic

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@CompileStatic
class ThreadsTestPropertiesProvider extends AbstractContextPropertiesProvider {

    private static final URL RES = ThreadsTestPropertiesProvider.class.getResource("threads_test.properties");

    ThreadsTestPropertiesProvider() {
        super('com.anrisoftware.globalpom.threads.properties.internal', RES);
    }
}
