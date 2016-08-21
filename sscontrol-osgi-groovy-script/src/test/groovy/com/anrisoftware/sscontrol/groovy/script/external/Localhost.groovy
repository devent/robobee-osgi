package com.anrisoftware.sscontrol.groovy.script.external

import com.anrisoftware.sscontrol.types.external.DebugLogging
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
}
