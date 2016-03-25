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
package com.anrisoftware.database.internal;

import static com.anrisoftware.database.internal.DatabaseImplLogger._.dbAdded;

import javax.inject.Singleton;

import com.anrisoftware.database.external.Database;
import com.anrisoftware.database.external.DatabaseDb;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link DatabaseImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class DatabaseImplLogger extends AbstractLogger {

    enum _ {

        dbAdded("Database {} added to {}");

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
     * Sets the context of the logger to {@link DatabaseImpl}.
     */
    public DatabaseImplLogger() {
        super(DatabaseImpl.class);
    }

    public void dbAdded(Database database, DatabaseDb db) {
        debug(dbAdded, db, database);
    }
}
