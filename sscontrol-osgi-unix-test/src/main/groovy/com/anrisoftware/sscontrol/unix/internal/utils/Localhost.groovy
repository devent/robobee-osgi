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
