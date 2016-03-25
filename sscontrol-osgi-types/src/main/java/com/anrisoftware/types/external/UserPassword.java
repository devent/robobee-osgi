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
package com.anrisoftware.types.external;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User name and password credentials.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class UserPassword {

    public final String name;

    public final String password;

    public UserPassword(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public UserPassword changeName(String name) {
        return new UserPassword(name, password);
    }

    public UserPassword changePassword(String password) {
        return new UserPassword(name, password);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
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
        if (!(obj instanceof UserPassword)) {
            return false;
        }
        UserPassword rhs = (UserPassword) obj;
        return new EqualsBuilder().append(name, rhs.name)
                .append(password, rhs.password).isEquals();
    }
}
