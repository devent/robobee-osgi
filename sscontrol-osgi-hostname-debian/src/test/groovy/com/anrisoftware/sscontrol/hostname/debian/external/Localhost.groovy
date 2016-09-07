package com.anrisoftware.sscontrol.hostname.debian.external

import com.anrisoftware.sscontrol.types.external.DebugLogging
import com.anrisoftware.sscontrol.types.external.HostServiceProperties
import com.anrisoftware.sscontrol.types.external.Ssh
import com.anrisoftware.sscontrol.types.external.SshHost

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class Localhost implements Ssh {

    @Override
    DebugLogging getDebugLogging() {
    }

    @Override
    List<SshHost> getTargets() {
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
    String getGroup() {
    }

    @Override
    List<SshHost> getHosts() {
    }

    @Override
    HostServiceProperties getServiceProperties() {
    }
}
