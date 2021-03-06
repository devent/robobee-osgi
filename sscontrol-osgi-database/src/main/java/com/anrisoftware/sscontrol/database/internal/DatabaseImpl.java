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

import static com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.stringListStatement;
import static org.apache.commons.lang3.Validate.notNull;
import static org.codehaus.groovy.runtime.InvokerHelper.invokeMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.strings.ToStringService;
import com.anrisoftware.sscontrol.database.external.Database;
import com.anrisoftware.sscontrol.database.external.DatabaseDb;
import com.anrisoftware.sscontrol.database.external.DatabaseService;
import com.anrisoftware.sscontrol.database.external.DatabaseUser;
import com.anrisoftware.sscontrol.database.internal.DatabaseDbImpl.DatabaseDbImplFactory;
import com.anrisoftware.sscontrol.database.internal.DatabaseUserImpl.DatabaseUserImplFactory;
import com.anrisoftware.sscontrol.debug.external.DebugService;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.ArgumentInvalidException;
import com.anrisoftware.sscontrol.types.external.BindingAddress;
import com.anrisoftware.sscontrol.types.external.BindingHost;
import com.anrisoftware.sscontrol.types.external.BindingHostService;
import com.anrisoftware.sscontrol.types.external.DebugLogging;
import com.anrisoftware.sscontrol.types.external.HostPropertiesService;
import com.anrisoftware.sscontrol.types.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.ListProperty;
import com.anrisoftware.sscontrol.types.external.UserPassword;
import com.anrisoftware.sscontrol.types.external.UserPasswordService;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Database service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class DatabaseImpl implements Database {

    public interface DatabaseImplFactory extends DatabaseService {

    }

    private final List<DatabaseDb> dbs;

    private final List<DatabaseUser> users;

    private final List<SshHost> targets;

    private final HostServiceProperties serviceProperties;

    @Inject
    private DatabaseImplLogger log;

    @Inject
    private DatabaseDbImplFactory dbFactory;

    @Inject
    private DatabaseUserImplFactory userFactory;

    @Inject
    private UserPasswordService userPasswordService;

    @Inject
    private ToStringService toStringService;

    private BindingHost binding;

    private UserPassword adminUser;

    private DebugLogging debug;

    @SuppressWarnings("unchecked")
    @AssistedInject
    DatabaseImpl(BindingHostService bindingHostService,
            HostPropertiesService propertiesService,
            @Assisted Map<String, Object> args) {
        this.targets = (List<SshHost>) args.get("targets");
        this.serviceProperties = propertiesService.create();
        this.dbs = new ArrayList<DatabaseDb>();
        this.users = new ArrayList<DatabaseUser>();
        this.binding = bindingHostService.create();
    }

    @Inject
    public void setDebugService(DebugService debugService) {
        this.debug = debugService.create();
    }

    public void binding(Map<String, Object> args, BindingAddress address)
            throws ArgumentInvalidException {
        Map<String, Object> a = new HashMap<String, Object>(args);
        a.put("host", address.getAddress());
        binding(a);
    }

    public void binding(Map<String, Object> args, String host)
            throws ArgumentInvalidException {
        Map<String, Object> a = new HashMap<String, Object>(args);
        a.put("host", host);
        binding(a);
    }

    public void binding(Map<String, Object> args)
            throws ArgumentInvalidException {
        ArgumentInvalidException.checkNullArg(args, "binding");
        invokeMethod(binding, "binding", args);
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
        invokeMethod(db, "db", a);
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
        invokeMethod(user, "user", a);
        users.add(user);
        log.userAdded(this, user);
        return user;
    }

    public void debug(Map<String, Object> args, String name) {
        Map<String, Object> arguments = new HashMap<String, Object>(args);
        arguments.put("name", name);
        invokeMethod(debug, "debug", arguments);
    }

    public void debug(Map<String, Object> args) {
        Map<String, Object> arguments = new HashMap<String, Object>(args);
        invokeMethod(debug, "debug", arguments);
    }

    public List<String> getProperty() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                serviceProperties.addProperty(property);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public List<Object> getDebug() {
        return (List<Object>) invokeMethod(debug, "getDebug", null);
    }

    @Override
    public void setBinding(BindingHost binding) {
        this.binding = binding;
    }

    @Override
    public BindingHost getBinding() {
        return binding;
    }

    @Override
    public void setAdminUser(UserPassword user) {
        this.adminUser = user;
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
    public DebugLogging getDebugLogging() {
        return debug;
    }

    @Override
    public List<SshHost> getTargets() {
        return targets;
    }

    @Override
    public HostServiceProperties getServiceProperties() {
        return serviceProperties;
    }

    @Override
    public String getName() {
        return "db";
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("targets", targets).append("bind", binding)
                .append("admin", adminUser).append("dbs", dbs)
                .append("users", users).toString();
    }
}
