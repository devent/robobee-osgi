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

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.database.external.Database;
import com.anrisoftware.database.external.DatabaseDb;
import com.anrisoftware.database.external.DatabaseUser;
import com.anrisoftware.database.internal.DatabaseDbImpl.DatabaseDbImplFactory;
import com.anrisoftware.database.internal.DatabaseUserImpl.DatabaseUserImplFactory;
import com.anrisoftware.types.external.UserPassword;

/**
 * Internal implementation of our example OSGi service
 */
public class DatabaseImpl implements Database {

    private final List<DatabaseDb> dbs;

    private final List<DatabaseUser> users;

    @Inject
    private DatabaseImplLogger log;

    @Inject
    private DatabaseDbImplFactory dbFactory;

    @Inject
    private DatabaseUserImplFactory userFactory;

    DatabaseImpl() {
        this.dbs = new ArrayList<DatabaseDb>();
        this.users = new ArrayList<DatabaseUser>();
    }

    public void bind(Map<String, Object> args, String host) {

    }

    public void admin(Map<String, Object> args) {

    }

    public DatabaseDb db(Map<String, Object> args) {
        DatabaseDb db = dbFactory.create(args);
        dbs.add(db);
        log.dbAdded(this, db);
        return db;
    }

    public DatabaseUser user(Map<String, Object> args, String name) {
        args.put("name", name);
        DatabaseUser user = userFactory.create(args);
        users.add(user);
        return user;
    }

    @Override
    public Database setBind(InetSocketAddress address) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Database setAdmin(UserPassword userPassword) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<DatabaseDb> getDatabases() {
        return Collections.unmodifiableList(dbs);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }
}
