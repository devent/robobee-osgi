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
package com.anrisoftware.sscontrol.database.internal;

import static com.anrisoftware.sscontrol.database.internal.DatabaseAccessImplLogger._.databaseSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link DatabaseAccessImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class DatabaseAccessImplLogger extends AbstractLogger {

    enum _ {

        databaseSet("Database '{}' set to {}");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link DatabaseAccessImpl}.
     */
    public DatabaseAccessImplLogger() {
        super(DatabaseAccessImpl.class);
    }

    void databaseSet(DatabaseAccessImpl access, String database) {
        debug(databaseSet, database, access);
    }
}
