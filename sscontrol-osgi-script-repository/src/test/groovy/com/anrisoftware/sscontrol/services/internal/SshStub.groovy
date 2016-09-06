package com.anrisoftware.sscontrol.services.internal

import groovy.transform.ToString

import javax.inject.Inject

import com.anrisoftware.sscontrol.types.external.DebugLogging
import com.anrisoftware.sscontrol.types.external.HostService
import com.anrisoftware.sscontrol.types.external.HostServiceProperties
import com.anrisoftware.sscontrol.types.external.HostServiceService
import com.anrisoftware.sscontrol.types.external.Ssh
import com.anrisoftware.sscontrol.types.external.SshHost
import com.google.inject.assistedinject.Assisted

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
    List<SshHost> getHosts() {
        hosts
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
