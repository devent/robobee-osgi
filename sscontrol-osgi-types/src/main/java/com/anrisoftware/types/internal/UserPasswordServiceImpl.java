/*
 * Copyright 2011-2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-types.
 *
 * sscontrol-osgi-types is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-types is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-types. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.types.internal;

import javax.inject.Inject;

import com.anrisoftware.types.external.UserPassword;
import com.anrisoftware.types.external.UserPasswordFactory;
import com.anrisoftware.types.external.UserPasswordService;

public class UserPasswordServiceImpl implements UserPasswordService {

    @Inject
    private UserPasswordFactory userPasswordFactory;

    @Override
    public UserPassword create(String name, String password) {
        return userPasswordFactory.create(name, password);
    }

}
