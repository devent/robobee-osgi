package com.anrisoftware.sscontrol.services.internal

import groovy.transform.ToString

import javax.inject.Inject

import com.anrisoftware.sscontrol.services.internal.HostServiceStub.HostServiceStubFactory
import com.anrisoftware.sscontrol.types.external.HostService
import com.anrisoftware.sscontrol.types.external.HostServiceService

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@ToString
class HostServiceStubServiceImpl implements HostServiceService {

    @Inject
    HostServiceStubFactory serviceFactory

    @Override
    HostService create() {
        serviceFactory.create()
    }
}
