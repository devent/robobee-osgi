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
class HostsStub implements HostService {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    interface HostsStubFactory {

        HostsStub create(Map<String, Object> args)
    }

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    @ToString
    static class HostsStubServiceImpl implements HostServiceService {

        @Inject
        HostsStubFactory serviceFactory

        @Override
        HostService create(Map<String, Object> args) {
            serviceFactory.create(args)
        }
    }

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    static class Host {

        String host

        String address
    }

    List<Host> hosts

    List<SshHost> targets

    @Inject
    HostsStub(@Assisted Map<String, Object> args) {
        this.targets = args.targets
        this.hosts = []
    }

    void host(def name, def address) {
        hosts.add new Host(host: name, address: address)
    }

    List<Host> getHosts() {
        hosts;
    }

    @Override
    List<SshHost> getTargets() {
        targets
    }

    @Override
    HostServiceProperties getServiceProperties() {
    }
}
