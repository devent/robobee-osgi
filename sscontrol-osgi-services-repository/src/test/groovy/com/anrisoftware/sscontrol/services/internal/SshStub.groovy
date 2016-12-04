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
package com.anrisoftware.sscontrol.services.internal

import javax.inject.Inject

import com.anrisoftware.sscontrol.types.external.DebugLogging
import com.anrisoftware.sscontrol.types.external.HostService
import com.anrisoftware.sscontrol.types.external.HostServiceProperties
import com.anrisoftware.sscontrol.types.external.HostServiceService
import com.anrisoftware.sscontrol.types.external.Ssh
import com.anrisoftware.sscontrol.types.external.SshHost
import com.google.inject.assistedinject.Assisted

import groovy.transform.ToString

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@ToString
class SshStub implements Ssh {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    interface SshStubFactory {

        SshStub create(Map<String, Object> args)
    }

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    @ToString
    static class SshStubServiceImpl implements HostServiceService {

        @Inject
        SshStubFactory serviceFactory

        @Override
        HostService create(Map<String, Object> args) {
            serviceFactory.create(args)
        }
    }

    String group

    List<SshHost> targets

    List<SshHost> hosts

    @Inject
    SshStub(@Assisted Map<String, Object> args) {
        this.targets = args.targets
        this.hosts = []
    }

    void group(String name) {
        this.group = name
    }

    String getGroup() {
        group
    }

    void host(String name) {
        hosts.add([
            getHost: { name }
        ] as SshHost)
    }

    @Override
    String getName() {
        'ssh'
    }

    @Override
    List<SshHost> getHosts() {
        hosts
    }

    @Override
    SshHost getTarget() {
        targets[0]
    }

    @Override
    List<SshHost> getTargets() {
        targets
    }

    @Override
    HostServiceProperties getServiceProperties() {
    }

    @Override
    DebugLogging getDebugLogging() {
        return null;
    }
}
