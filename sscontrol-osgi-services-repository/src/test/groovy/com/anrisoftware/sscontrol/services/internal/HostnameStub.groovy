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

import com.anrisoftware.sscontrol.types.external.HostService
import com.anrisoftware.sscontrol.types.external.HostServiceProperties
import com.anrisoftware.sscontrol.types.external.HostServiceService
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
class HostnameStub implements HostService {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    interface HostnameStubFactory {

        HostnameStub create(Map<String, Object> args)
    }

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    @ToString
    static class HostnameStubServiceImpl implements HostServiceService {

        @Inject
        HostnameStubFactory serviceFactory

        @Override
        HostService create(Map<String, Object> args) {
            serviceFactory.create(args)
        }
    }

    String name

    List<SshHost> targets

    @Inject
    HostnameStub(@Assisted Map<String, Object> args) {
        this.targets = args.targets
    }

    void set(String name) {
        this.name = name
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
}
