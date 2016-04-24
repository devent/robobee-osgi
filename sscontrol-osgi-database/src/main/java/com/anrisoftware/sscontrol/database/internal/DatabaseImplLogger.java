/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.database.internal;

import static com.anrisoftware.sscontrol.database.internal.DatabaseImplLogger._.adminSet;
import static com.anrisoftware.sscontrol.database.internal.DatabaseImplLogger._.dbAdded;
import static com.anrisoftware.sscontrol.database.internal.DatabaseImplLogger._.userAdded;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.database.external.Database;
import com.anrisoftware.sscontrol.database.external.DatabaseDb;
import com.anrisoftware.sscontrol.database.external.DatabaseUser;
import com.anrisoftware.sscontrol.types.external.UserPassword;

/**
 * Logging for {@link DatabaseImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class DatabaseImplLogger extends AbstractLogger {

    enum _ {

        dbAdded("Database {} added to {}"),

        adminSet("Admin user {} set to {}"),

        userAdded("User {} added to {}");

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

    void adminSet(DatabaseImpl database, UserPassword admin) {
        debug(adminSet, admin, database);
    }

    void userAdded(DatabaseImpl database, DatabaseUser user) {
        debug(userAdded, user, database);
    }

}
