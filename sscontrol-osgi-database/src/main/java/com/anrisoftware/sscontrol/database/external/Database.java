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
package com.anrisoftware.sscontrol.database.external;

import java.net.InetSocketAddress;
import java.util.List;

import com.anrisoftware.sscontrol.types.external.UserPassword;

/**
 * Public API representing an example OSGi service
 */
public interface Database {

    Database setBindAddress(InetSocketAddress address);

    InetSocketAddress getBindAddress();

    Database setAdminUser(UserPassword userPassword);

    UserPassword getAdminUser();

    List<DatabaseDb> getDatabases();

    List<DatabaseUser> getUsers();
}