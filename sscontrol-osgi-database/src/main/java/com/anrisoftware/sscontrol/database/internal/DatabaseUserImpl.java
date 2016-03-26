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
