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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.types.external.UserPassword;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * User name and password credentials.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class UserPasswordImpl implements UserPassword {

    private final String name;

    private final String password;

    @AssistedInject
    UserPasswordImpl() {
        this.name = null;
        this.password = null;
    }

    @AssistedInject
    UserPasswordImpl(@Assisted("name") String name,
            @Assisted("password") String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public UserPassword changeName(String name) {
        return new UserPasswordImpl(name, password);
    }

    @Override
    public UserPassword changePassword(String password) {
        return new UserPasswordImpl(name, password);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name)
                .append("password", password).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(password).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UserPasswordImpl)) {
            return false;
        }
        UserPasswordImpl rhs = (UserPasswordImpl) obj;
        return new EqualsBuilder().append(name, rhs.name)
                .append(password, rhs.password).isEquals();
    }
}
