package com.anrisoftware.sscontrol.services.internal

import groovy.transform.ToString

import javax.inject.Inject

import com.anrisoftware.sscontrol.types.external.HostService
import com.anrisoftware.sscontrol.types.external.HostServiceProperties
import com.anrisoftware.sscontrol.types.external.HostServiceService
import com.anrisoftware.sscontrol.types.external.SshHost
import com.google.inject.assistedinject.Assisted

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
    List<SshHost> getTargets() {
        targets
    }

    @Override
    HostServiceProperties getServiceProperties() {
    }
}
