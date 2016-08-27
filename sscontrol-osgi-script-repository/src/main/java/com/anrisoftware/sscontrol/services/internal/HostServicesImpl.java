/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.services.internal;

import static java.util.Collections.synchronizedList;
import static java.util.Collections.synchronizedMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.types.external.HostService;
import com.anrisoftware.sscontrol.types.external.HostServiceService;
import com.anrisoftware.sscontrol.types.external.HostServices;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Host services repository.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class HostServicesImpl implements HostServices {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface HostServicesImplFactory {

        HostServicesImpl create();

        HostServicesImpl create(@Assisted HostServices repository);

    }

    private final Map<String, HostServiceService> availableServices;

    private final Map<String, List<HostService>> hostServices;

    @Inject
    private HostServicesImplLogger log;

    @AssistedInject
    HostServicesImpl() {
        this.availableServices = synchronizedMap(
                new HashMap<String, HostServiceService>());
        this.hostServices = synchronizedMap(
                new LinkedHashMap<String, List<HostService>>());
    }

    @AssistedInject
    HostServicesImpl(@Assisted HostServices services) {
        this();
        copyAvailableServices(availableServices, services);
    }

    public HostService call(String name) {
        HostServiceService service = availableServices.get(name);
        HostService hostService = service.create();
        addService(name, hostService);
        return hostService;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends HostServiceService> T getAvailableService(String name) {
        return (T) availableServices.get(name);
    }

    @Override
    public Set<String> getAvailableServices() {
        return availableServices.keySet();
    }

    @Override
    public void putAvailableService(String name, HostServiceService service) {
        availableServices.put(name, service);
        log.availableServiceAdded(this, name, service);
    }

    @Override
    public void removeAvailableService(String name) {
        availableServices.remove(name);
    }

    private void copyAvailableServices(Map<String, HostServiceService> scripts,
            HostServices repository) {
        for (String name : repository.getAvailableServices()) {
            scripts.put(name, repository.getAvailableService(name));
        }
    }

    @Override
    public List<HostService> getServices(String name) {
        return hostServices.get(name);
    }

    @Override
    public Set<String> getServices() {
        return hostServices.keySet();
    }

    @Override
    public void addService(String name, HostService service) {
        List<HostService> services = hostServices.get(name);
        if (services == null) {
            services = synchronizedList(new ArrayList<HostService>());
            hostServices.put(name, services);
        }
        services.add(service);
        log.addService(this, name, service);
    }

    @Override
    public void removeService(String name, int index) {
        List<HostService> services = hostServices.get(name);
        if (services != null) {
            services.remove(index);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("available service", getAvailableServices())
                .append("services", getServices()).toString();
    }

}
