package com.anrisoftware.sscontrol.profile.internal

import static com.anrisoftware.sscontrol.types.external.HostServicePropertiesUtil.propertyStatement
import groovy.transform.ToString

import javax.inject.Inject

import com.anrisoftware.sscontrol.types.external.HostPropertiesService
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
class PropertiesStub implements HostService {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    interface PropertiesStubFactory {

        PropertiesStub create(Map<String, Object> args)
    }

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    @ToString
    static class PropertiesStubServiceImpl implements HostServiceService {

        @Inject
        PropertiesStubFactory serviceFactory

        @Override
        HostService create(Map<String, Object> args) {
            serviceFactory.create(args)
        }
    }

    HostServiceProperties serviceProperties

    @Inject
    PropertiesStub(HostPropertiesService propertiesService, @Assisted Map<String, Object> args) {
        this.serviceProperties = propertiesService.create();
    }

    @Override
    List<SshHost> getTargets() {
    }

    @Override
    HostServiceProperties getServiceProperties() {
        serviceProperties
    }

    List<String> getProperty() {
        return propertyStatement(serviceProperties);
    }
}
