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

import static com.anrisoftware.sscontrol.database.internal.DatabaseUserImplLogger._.accessAdded;
import static com.anrisoftware.sscontrol.database.internal.DatabaseUserImplLogger._.userPasswordSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.database.external.DatabaseAccess;
import com.anrisoftware.sscontrol.types.external.UserPassword;

/**
 * Logging for {@link DatabaseUserImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class DatabaseUserImplLogger extends AbstractLogger {

    enum _ {

        userPasswordSet("User password {} set to {}"),

        accessAdded("Access {} added to {}");

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
     * Sets the context of the logger to {@link DatabaseUserImpl}.
     */
    public DatabaseUserImplLogger() {
        super(DatabaseUserImpl.class);
    }

    void userPasswordSet(DatabaseUserImpl databaseUser,
            UserPassword userPassword) {
        debug(userPasswordSet, userPassword, databaseUser);
    }

    void accessAdded(DatabaseUserImpl databaseUser, DatabaseAccess access) {
        debug(accessAdded, access, databaseUser);
    }
}
