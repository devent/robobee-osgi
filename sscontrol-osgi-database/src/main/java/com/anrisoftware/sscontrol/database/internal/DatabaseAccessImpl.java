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

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.database.external.DatabaseAccess;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.ToStringService;

public class DatabaseAccessImpl implements DatabaseAccess {

    public interface DatabaseAccessImplFactory {

        DatabaseAccessImpl create();

    }

    @Inject
    private DatabaseAccessImplLogger log;

    @Inject
    private ToStringService toString;

    private String database;

    public void access(Map<String, Object> args) throws AppException {
        this.database = toString.toString(args, "database");
        log.databaseSet(this, database);
    }

    @Override
    public String getDatabase() {
        return database;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("db", database).toString();
    }
}
