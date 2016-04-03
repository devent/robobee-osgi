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
