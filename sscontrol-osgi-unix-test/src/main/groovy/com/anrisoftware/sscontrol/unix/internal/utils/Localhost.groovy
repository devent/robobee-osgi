/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-unix-test.
 *
 * sscontrol-osgi-unix-test is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-unix-test is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-unix-test. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.unix.internal.utils

import groovy.transform.ToString

import javax.inject.Inject

import com.anrisoftware.sscontrol.types.external.DebugLogging
import com.anrisoftware.sscontrol.types.external.HostPropertiesService
import com.anrisoftware.sscontrol.types.external.HostServiceProperties
import com.anrisoftware.sscontrol.types.external.Ssh
import com.anrisoftware.sscontrol.types.external.SshHost

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@ToString
class Localhost implements Ssh {

    def serviceProperties

    @Inject
    Localhost(HostPropertiesService propertiesService) {
        this.serviceProperties = propertiesService.create()
    }

    @Override
    String getName() {
        return "ssh"
    }

    @Override
    DebugLogging getDebugLogging() {
    }

    @Override
    List<SshHost> getTargets() {
    }

    @Override
    String getGroup() {
    }

    @Override
    List<SshHost> getHosts() {
        [
            [
                getHost: { 'localhost' },
                getUser: { System.getProperty('user.name') },
                getPort: { 22 },
                getKey: {
                }
            ] as SshHost
        ]
    }

    @Override
    HostServiceProperties getServiceProperties() {
        return serviceProperties
    }
}
