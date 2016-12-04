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
package com.anrisoftware.sscontrol.hosts.internal;

import static com.anrisoftware.sscontrol.hosts.internal.HostsServiceImpl.HOSTS_NAME;
import static com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.stringListStatement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.arrays.ToList;
import com.anrisoftware.sscontrol.hosts.external.Host;
import com.anrisoftware.sscontrol.hosts.external.Hosts;
import com.anrisoftware.sscontrol.hosts.external.HostsService;
import com.anrisoftware.sscontrol.hosts.internal.HostImpl.HostImplFactory;
import com.anrisoftware.sscontrol.types.external.HostPropertiesService;
import com.anrisoftware.sscontrol.types.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Hosts service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class HostsImpl implements Hosts {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface HostsImplFactory extends HostsService {

    }

    private final HostsImplLogger log;

    private final List<Host> hosts;

    private final HostServiceProperties serviceProperties;

    private final List<SshHost> targets;

    private final HostImplFactory hostFactory;

    @AssistedInject
    HostsImpl(HostsImplLogger log, HostImplFactory hostFactory,
            HostPropertiesService propertiesService,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.hostFactory = hostFactory;
        this.targets = new ArrayList<SshHost>();
        this.hosts = new ArrayList<Host>();
        this.serviceProperties = propertiesService.create();
        parseArgs(args);
    }

    public void ip(Map<String, Object> args, String address) {
        Map<String, Object> a = new HashMap<String, Object>(args);
        a.put("ip", address);
        ip(a);
    }

    public void ip(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<String, Object>(args);
        List<String> aliases = new ArrayList<String>();
        a.put("address", a.get("ip"));
        a.put("aliases", aliases);
        if (a.get("alias") != null) {
            aliases = ToList.toList(args.get("alias"));
            a.put("aliases", aliases);
        }
        a.put("identifier", a.get("on"));
        if (a.get("on") == null) {
            a.put("identifier", "host");
        }
        Host h = hostFactory.create(a);
        log.hostAdded(this, h);
        hosts.add(h);
    }

    @Override
    public String getName() {
        return HOSTS_NAME;
    }

    @Override
    public List<Host> getHosts() {
        return hosts;
    }

    @Override
    public SshHost getTarget() {
        return getTargets().get(0);
    }

    @Override
    public List<SshHost> getTargets() {
        return targets;
    }

    public List<String> getProperty() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                serviceProperties.addProperty(property);
            }
        });
    }

    @Override
    public HostServiceProperties getServiceProperties() {
        return serviceProperties;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("hosts", hosts).append("targets", targets).toString();
    }

    @SuppressWarnings("unchecked")
    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("targets");
        if (v != null) {
            targets.addAll((List<SshHost>) v);
        }
        if (args.get("ip") != null) {
            ip(args);
        }
    }

}
