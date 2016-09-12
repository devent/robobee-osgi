package com.anrisoftware.sscontrol.groovy.script.external

import groovy.transform.ToString

import com.anrisoftware.sscontrol.types.external.SshHost

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@ToString
class Localhost implements SshHost {

    @Override
    String getHost() {
        'localhost'
    }

    @Override
    String getUser() {
        System.getProperty('user.name')
    }

    @Override
    Integer getPort() {
        22
    }

    @Override
    URI getKey() {
    }
}
