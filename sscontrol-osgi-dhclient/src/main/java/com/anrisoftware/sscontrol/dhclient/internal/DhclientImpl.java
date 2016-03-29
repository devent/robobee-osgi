/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-dhclient.
 *
 * sscontrol-osgi-dhclient is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-dhclient is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-dhclient. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dhclient.internal;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.dhclient.external.Dhclient;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Internal implementation of our example OSGi service
 */
public class DhclientImpl implements Dhclient {

    public interface DhclientImplFactory {

        DhclientImpl create();

        DhclientImpl create(@Assisted Dhclient database);

    }

    @Inject
    private DhclientImplLogger log;

    @Inject
    private DhclientImplFactory databaseFactory;

    @AssistedInject
    DhclientImpl() {
    }

    @AssistedInject
    DhclientImpl(@Assisted Dhclient database) {
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }
}
