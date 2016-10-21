/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-unix.
 *
 * sscontrol-osgi-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-unix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.unix.internal.core;

import groovy.transform.CompileStatic

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@CompileStatic
class CmdThreadsTestPropertiesProvider extends AbstractContextPropertiesProvider {

    private static final URL RES = CmdThreadsTestPropertiesProvider.class.getResource("cmd_threads_test.properties");

    CmdThreadsTestPropertiesProvider() {
        super('com.anrisoftware.globalpom.threads.properties.internal', RES);
    }
}
