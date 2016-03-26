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
