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
package com.anrisoftware.sscontrol.types.internal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.types.external.UserPassword;
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
