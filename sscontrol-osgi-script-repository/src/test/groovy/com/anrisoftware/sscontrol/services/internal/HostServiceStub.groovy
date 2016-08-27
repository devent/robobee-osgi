package com.anrisoftware.sscontrol.services.internal

import groovy.transform.ToString

import com.anrisoftware.sscontrol.types.external.HostService

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@ToString
class HostServiceStub implements HostService {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    interface HostServiceStubFactory {

        HostServiceStub create()
    }

    String name

    void set(String name) {
        this.name = name
    }
}
