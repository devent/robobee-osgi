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
package com.anrisoftware.sscontrol.shell.external.utils

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
