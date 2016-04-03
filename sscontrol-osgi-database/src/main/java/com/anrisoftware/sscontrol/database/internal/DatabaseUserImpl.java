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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.database.external.DatabaseAccess;
import com.anrisoftware.sscontrol.database.external.DatabaseUser;
import com.anrisoftware.sscontrol.database.internal.DatabaseAccessImpl.DatabaseAccessImplFactory;
import com.anrisoftware.sscontrol.types.external.UserPassword;
import com.anrisoftware.sscontrol.types.external.UserPasswordService;
import com.google.inject.assistedinject.Assisted;

public class DatabaseUserImpl implements DatabaseUser {

    public interface DatabaseUserImplFactory {

        DatabaseUserImpl create(Map<String, Object> args);

    }

    private final List<DatabaseAccess> accesses;

    private final UserPassword userPassword;

    @Inject
    private DatabaseUserImplLogger log;

    @Inject
    private DatabaseAccessImplFactory accessFactory;

    @Inject
    DatabaseUserImpl(@Assisted Map<String, Object> args,
            UserPasswordService userPasswordService, DatabaseUserImplLogger log) {
        this.accesses = new ArrayList<DatabaseAccess>();
        this.userPassword = toUserPassword(args, userPasswordService, log);
    }

    @SuppressWarnings("deprecation")
    private UserPassword toUserPassword(Map<String, Object> args,
            UserPasswordService userPasswordService, DatabaseUserImplLogger log) {
        notNull(args.get("name"), "name=null");
        notNull(args.get("password"), "password=null");
        String name = ObjectUtils.toString(args.get("name"));
        String password = ObjectUtils.toString(args.get("password"));
        UserPassword userPassword = userPasswordService.create(name, password);
        log.userPasswordSet(this, userPassword);
        return userPassword;
    }

    public void access(Map<String, Object> args) {
        DatabaseAccess access = accessFactory.create(args);
        log.accessAdded(this, access);
        accesses.add(access);
    }

    @Override
    public UserPassword getUser() {
        return userPassword;
    }

    @Override
    public List<DatabaseAccess> getAccess() {
        return Collections.unmodifiableList(accesses);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("user", userPassword)
                .toString();
    }
}
