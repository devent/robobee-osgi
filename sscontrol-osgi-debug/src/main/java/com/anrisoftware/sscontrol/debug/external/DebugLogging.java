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
package com.anrisoftware.sscontrol.debug.external;

import java.util.Map;

/**
 * Debug logging.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface DebugLogging {

    /**
     * Returns the modules for which the logging is active.
     */
    Map<String, DebugModule> getModules();

    /**
     * Adds the specified logging module.
     */
    DebugLogging putModule(DebugModule module);

    /**
     * Removes the specified logging module.
     */
    DebugLogging removeModule(String name);

}
