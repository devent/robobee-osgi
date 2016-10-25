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
package com.anrisoftware.sscontrol.unix.external.utils

import javax.inject.Inject

import com.anrisoftware.sscontrol.types.external.DebugLogging
import com.anrisoftware.sscontrol.types.external.HostPropertiesService
import com.anrisoftware.sscontrol.types.external.HostServiceProperties
import com.anrisoftware.sscontrol.types.external.Ssh
import com.anrisoftware.sscontrol.types.external.SshHost
import com.google.inject.Injector

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class SshFactory implements Ssh {

    static Ssh localhost(Injector injector) {
        def ssh = injector.getInstance(SshFactory)
        ssh.hosts = [
            [
                getHost: { 'localhost' },
                getUser: { System.getProperty('user.name') },
                getPort: { 22 },
                getKey: {
                }
            ] as SshHost
        ]
        return ssh
    }

    static Ssh testServer(Injector injector) {
        def ssh = injector.getInstance(SshFactory)
        ssh.hosts = [
            [
                getHost: { 'robobee' },
                getUser: { 'robobee' },
                getPort: { 22 },
                getKey: { UnixTestUtil.robobeeKey.toURI() }
            ] as SshHost
        ]
        return ssh
    }

    List<SshHost> hosts

    def serviceProperties

    @Inject
    SshFactory(HostPropertiesService propertiesService) {
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

    void setHosts(List<SshHost> hosts) {
        this.hosts = hosts
    }

    @Override
    List<SshHost> getHosts() {
        hosts
    }

    @Override
    HostServiceProperties getServiceProperties() {
        return serviceProperties
    }
}
