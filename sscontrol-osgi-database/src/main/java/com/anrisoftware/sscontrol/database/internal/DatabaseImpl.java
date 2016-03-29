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

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.felix.scr.annotations.Component;

import com.anrisoftware.sscontrol.database.external.Database;
import com.anrisoftware.sscontrol.database.external.DatabaseDb;
import com.anrisoftware.sscontrol.database.external.DatabaseUser;
import com.anrisoftware.sscontrol.database.internal.DatabaseDbImpl.DatabaseDbImplFactory;
import com.anrisoftware.sscontrol.database.internal.DatabaseUserImpl.DatabaseUserImplFactory;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.ToStringService;
import com.anrisoftware.sscontrol.types.external.UserPassword;
import com.anrisoftware.sscontrol.types.external.UserPasswordService;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Internal implementation of our example OSGi service
 */
@Component(name = "com.anrisoftware.database.internal", immediate = true)
public class DatabaseImpl implements Database {

    public interface DatabaseImplFactory {

        DatabaseImpl create();

        DatabaseImpl create(@Assisted Database database);

    }

    private final List<DatabaseDb> dbs;

    private final List<DatabaseUser> users;

    @Inject
    private DatabaseImplLogger log;

    @Inject
    private DatabaseImplFactory databaseFactory;

    @Inject
    private DatabaseDbImplFactory dbFactory;

    @Inject
    private DatabaseUserImplFactory userFactory;

    @Inject
    private UserPasswordService userPasswordService;

    @Inject
    private ToStringService toStringService;


    private InetSocketAddress bindAddress;

    private UserPassword adminUser;

    @AssistedInject
    DatabaseImpl() {
        this.dbs = new ArrayList<DatabaseDb>();
        this.users = new ArrayList<DatabaseUser>();
    }

    @AssistedInject
    DatabaseImpl(@Assisted Database database) {
        this.dbs = new ArrayList<DatabaseDb>(database.getDatabases());
        this.users = new ArrayList<DatabaseUser>(database.getUsers());
        this.bindAddress = database.getBindAddress();
        this.adminUser = database.getAdminUser();
    }

    public void bind(Map<String, Object> args, String host) throws AppException {
        args.put("host", host);
        bind(args);
    }

    private void bind(Map<String, Object> args) throws AppException {
        String host = toStringService.toString(args, "host");
        Integer port = (Integer) args.get("port");
        this.bindAddress = new InetSocketAddress(host, port);
        log.bindSet(this, bindAddress);
    }

    @SuppressWarnings("deprecation")
    public void admin(Map<String, Object> args) {
        notNull(args.get("user"), "user=null");
        notNull(args.get("password"), "password=null");
        String name = ObjectUtils.toString(args.get("user"));
        String password = ObjectUtils.toString(args.get("password"));
        this.adminUser = userPasswordService.create(name, password);
        log.adminSet(this, adminUser);
    }

    public DatabaseDb db(Map<String, Object> args) {
        DatabaseDb db = dbFactory.create(args);
        dbs.add(db);
        log.dbAdded(this, db);
        return db;
    }

    public DatabaseUser user(Map<String, Object> args, String name) {
        args.put("name", name);
        return user((LinkedHashMap<String, Object>) args);
    }

    public DatabaseUser user(LinkedHashMap<String, Object> args) {
        DatabaseUser user = userFactory.create(args);
        users.add(user);
        log.userAdded(this, user);
        return user;
    }

    public void debug(Map<String, Object> args, String name) {

    }

    public void debug(Map<String, Object> args) {

    }

    @Override
    public Database setBindAddress(InetSocketAddress address) {
        DatabaseImpl database = databaseFactory.create(this);
        database.bindAddress = address;
        return database;
    }

    @Override
    public InetSocketAddress getBindAddress() {
        return bindAddress;
    }

    @Override
    public Database setAdminUser(UserPassword user) {
        DatabaseImpl database = databaseFactory.create(this);
        database.adminUser = user;
        return database;
    }

    @Override
    public UserPassword getAdminUser() {
        return adminUser;
    }

    @Override
    public List<DatabaseDb> getDatabases() {
        return Collections.unmodifiableList(dbs);
    }

    @Override
    public List<DatabaseUser> getUsers() {
        return Collections.unmodifiableList(users);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("bind", bindAddress)
                .append("admin", adminUser).toString();
    }
}
