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

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.sscontrol.database.external.Database;
import com.anrisoftware.sscontrol.database.external.DatabaseDb;
import com.anrisoftware.sscontrol.database.external.DatabaseUser;
import com.anrisoftware.sscontrol.database.internal.DatabaseDbImpl.DatabaseDbImplFactory;
import com.anrisoftware.sscontrol.database.internal.DatabaseUserImpl.DatabaseUserImplFactory;
import com.anrisoftware.sscontrol.debug.external.DebugLogging;
import com.anrisoftware.sscontrol.debug.external.DebugService;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.ToStringService;
import com.anrisoftware.sscontrol.types.external.UserPassword;
import com.anrisoftware.sscontrol.types.external.UserPasswordService;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Database script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
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

    private DebugLogging debug;

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

    @Inject
    public void setDebugService(DebugService debugService) {
        this.debug = debugService.create();
    }

    public void bind(Map<String, Object> args, String host) throws AppException {
        args.put("host", host);
        bind(args);
    }

    /**
     * Sets the binding host.
     * <ul>
     * <li>{@code "host"} the host address.
     * <li>{@code "port"} the host port.
     * </ul>
     */
    public void bind(Map<String, Object> args) throws AppException {
        String host = toStringService.toString(args, "host");
        Integer port = (Integer) args.get("port");
        this.bindAddress = new InetSocketAddress(host, port);
        log.bindSet(this, bindAddress);
    }

    public void admin(Map<String, Object> args) throws AppException {
        notNull(args.get("user"), "user=null");
        notNull(args.get("password"), "password=null");
        String name = toStringService.toString(args, "user");
        String password = toStringService.toString(args, "password");
        this.adminUser = userPasswordService.create(name, password);
        log.adminSet(this, adminUser);
    }

    public DatabaseDb db(Map<String, Object> args, String name) {
        Map<String, Object> a = new HashMap<String, Object>(args);
        a.put("name", name);
        return db(a);
    }

    public DatabaseDb db(String name) {
        Map<String, Object> a = new HashMap<String, Object>();
        a.put("name", name);
        return db(a);
    }

    public DatabaseDb db(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<String, Object>(args);
        DatabaseDb db = dbFactory.create();
        InvokerHelper.invokeMethod(db, "db", a);
        dbs.add(db);
        log.dbAdded(this, db);
        return db;
    }

    public DatabaseUser user(Map<String, Object> args, String name) {
        Map<String, Object> a = new HashMap<String, Object>(args);
        a.put("name", name);
        return user(a);
    }

    public DatabaseUser user(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<String, Object>(args);
        DatabaseUser user = userFactory.create();
        InvokerHelper.invokeMethod(user, "user", a);
        users.add(user);
        log.userAdded(this, user);
        return user;
    }

    public void debug(Map<String, Object> args, String name) {
        Map<String, Object> arguments = new HashMap<String, Object>(args);
        arguments.put("name", name);
        InvokerHelper.invokeMethod(debug, "debug", arguments);
    }

    public void debug(Map<String, Object> args) {
        Map<String, Object> arguments = new HashMap<String, Object>(args);
        InvokerHelper.invokeMethod(debug, "debug", arguments);
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
    public DebugLogging getDebug() {
        return debug;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("bind", bindAddress)
                .append("admin", adminUser).append("dbs", dbs)
                .append("users", users).toString();
    }
}
