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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.sscontrol.database.external.DatabaseAccess;
import com.anrisoftware.sscontrol.database.external.DatabaseUser;
import com.anrisoftware.sscontrol.database.internal.DatabaseAccessImpl.DatabaseAccessImplFactory;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.ToStringService;
import com.anrisoftware.sscontrol.types.external.UserPassword;
import com.anrisoftware.sscontrol.types.external.UserPasswordService;

public class DatabaseUserImpl implements DatabaseUser {

    public interface DatabaseUserImplFactory {

        DatabaseUserImpl create();

    }

    private final List<DatabaseAccess> accesses;

    @Inject
    private DatabaseUserImplLogger log;

    @Inject
    private DatabaseAccessImplFactory accessFactory;

    @Inject
    private UserPasswordService userPasswordService;

    @Inject
    private ToStringService toString;

    private UserPassword user;

    @Inject
    DatabaseUserImpl() {
        this.accesses = new ArrayList<DatabaseAccess>();
    }

    public DatabaseUser user(Map<String, Object> args) throws AppException {
        String name = toString.toString(args, "name");
        String password = toString.toString(args, "password");
        this.user = userPasswordService.create(name, password);
        log.userPasswordSet(this, user);
        return this;
    }

    public void access(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<String, Object>(args);
        DatabaseAccess access = accessFactory.create();
        InvokerHelper.invokeMethodSafe(access, "access", a);
        log.accessAdded(this, access);
        accesses.add(access);
    }

    @Override
    public UserPassword getUser() {
        return user;
    }

    @Override
    public List<DatabaseAccess> getAccess() {
        return Collections.unmodifiableList(accesses);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("user", user).toString();
    }
}
