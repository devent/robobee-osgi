/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
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

import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.database.external.DatabaseAccess;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

public class DatabaseAccessImpl implements DatabaseAccess {

    public interface DatabaseAccessImplFactory {

        DatabaseAccessImpl create(Map<String, Object> args);

    }

    private final String database;

    @AssistedInject
    public DatabaseAccessImpl(@Assisted Map<String, Object> args,
            DatabaseAccessImplLogger log) {
        this.database = toDatabase(args, log);
    }

    @SuppressWarnings("deprecation")
    private String toDatabase(Map<String, Object> args,
            DatabaseAccessImplLogger log) {
        notNull(args.get("database"), "database=null");
        String database = ObjectUtils.toString(args.get("database"));
        log.databaseSet(this, database);
        return database;
    }

    @Override
    public String getDatabase() {
        return database;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("database", database)
                .toString();
    }
}
