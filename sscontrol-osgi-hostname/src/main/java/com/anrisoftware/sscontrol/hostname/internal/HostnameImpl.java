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
package com.anrisoftware.sscontrol.hostname.internal;

import static com.anrisoftware.sscontrol.hostname.internal.HostnameServiceImpl.HOSTNAME_NAME;
import static com.anrisoftware.sscontrol.types.external.HostServicePropertiesUtil.propertyStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.hostname.external.Hostname;
import com.anrisoftware.sscontrol.hostname.external.HostnameService;
import com.anrisoftware.sscontrol.types.external.HostPropertiesService;
import com.anrisoftware.sscontrol.types.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Hostname service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class HostnameImpl implements Hostname {

    public interface HostnameImplFactory extends HostnameService {

    }

    private final HostnameImplLogger log;

    private final List<SshHost> targets;

    private final HostServiceProperties serviceProperties;

    private String hostname;

    @AssistedInject
    HostnameImpl(HostnameImplLogger log,
            HostPropertiesService propertiesService,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.targets = new ArrayList<SshHost>();
        this.serviceProperties = propertiesService.create();
        parseArgs(args);
    }

    public void set(Map<String, Object> args) {
        parseArgs(args);
    }

    public void setFqdn(String fqdn) {
        this.hostname = fqdn;
        log.hostnameSet(this, fqdn);
    }

    @Override
    public String getName() {
        return HOSTNAME_NAME;
    }

    @Override
    public String getHostname() {
        return hostname;
    }

    @Override
    public List<SshHost> getTargets() {
        return targets;
    }

    public List<String> getProperty() {
        return propertyStatement(serviceProperties);
    }

    @Override
    public HostServiceProperties getServiceProperties() {
        return serviceProperties;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("hostname", hostname).append("hosts", targets)
                .toString();
    }

    @SuppressWarnings("unchecked")
    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("targets");
        if (v != null) {
            targets.addAll((List<SshHost>) v);
        }
        v = args.get("fqdn");
        if (v != null) {
            setFqdn(v.toString());
        }
    }

}
