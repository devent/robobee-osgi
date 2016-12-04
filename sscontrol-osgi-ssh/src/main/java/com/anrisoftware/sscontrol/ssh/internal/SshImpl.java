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
package com.anrisoftware.sscontrol.ssh.internal;

import static com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.stringListStatement;
import static org.codehaus.groovy.runtime.InvokerHelper.invokeMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.debug.external.DebugService;
import com.anrisoftware.sscontrol.ssh.external.SshService;
import com.anrisoftware.sscontrol.ssh.internal.SshHostImpl.SshHostImplFactory;
import com.anrisoftware.sscontrol.types.external.DebugLogging;
import com.anrisoftware.sscontrol.types.external.HostPropertiesService;
import com.anrisoftware.sscontrol.types.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.external.Ssh;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Ssh</i> script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class SshImpl implements Ssh {

    public interface SshImplFactory extends SshService {

    }

    private final List<SshHost> targets;

    private final HostServiceProperties serviceProperties;

    private final List<SshHost> hosts;

    private final SshImplLogger log;

    @Inject
    private SshHostImplFactory sshHostFactory;

    private DebugLogging debug;

    private String group;

    @AssistedInject
    SshImpl(SshImplLogger log, HostPropertiesService propertiesService,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.targets = new ArrayList<SshHost>();
        this.hosts = new ArrayList<SshHost>();
        this.serviceProperties = propertiesService.create();
        parseArgs(args);
    }

    @Inject
    public void setDebugService(DebugService debugService) {
        this.debug = debugService.create();
    }

    public List<String> getProperty() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                serviceProperties.addProperty(property);
            }
        });
    }

    public void group(String group) {
        setGroup(group);
    }

    public void setGroup(String group) {
        this.group = group;
        log.groupSet(this, group);
    }

    @Override
    public String getGroup() {
        return group;
    }

    public void debug(Map<String, Object> args, String name) {
        Map<String, Object> arguments = new HashMap<String, Object>(args);
        arguments.put("name", name);
        invokeMethod(debug, "debug", arguments);
    }

    public void debug(Map<String, Object> args) {
        Map<String, Object> arguments = new HashMap<String, Object>(args);
        invokeMethod(debug, "debug", arguments);
    }

    public void host(String host) {
        SshHost sshHost = sshHostFactory.create();
        invokeMethod(sshHost, "host", host);
        this.hosts.add(sshHost);
        log.hostAdded(this, sshHost);
    }

    public void host(Map<String, Object> args) {
        Map<String, Object> arguments = new HashMap<String, Object>(args);
        SshHost sshHost = sshHostFactory.create();
        invokeMethod(sshHost, "host", arguments);
        this.hosts.add(sshHost);
        log.hostAdded(this, sshHost);
    }

    public List<String> getHost() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                host(property);
            }
        });
    }

    @Override
    public List<SshHost> getHosts() {
        return hosts;
    }

    @SuppressWarnings("unchecked")
    public List<Object> getDebug() {
        return (List<Object>) invokeMethod(debug, "getDebug", null);
    }

    @Override
    public DebugLogging getDebugLogging() {
        return debug;
    }

    @Override
    public SshHost getTarget() {
        return getTargets().get(0);
    }

    @Override
    public List<SshHost> getTargets() {
        return Collections.unmodifiableList(targets);
    }

    @Override
    public HostServiceProperties getServiceProperties() {
        return serviceProperties;
    }

    @Override
    public String getName() {
        return "ssh";
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("group", group).append("hosts", hosts.size())
                .toString();
    }

    @SuppressWarnings("unchecked")
    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("targets");
        if (v != null) {
            targets.addAll((List<SshHost>) v);
        }
        v = args.get("group");
        if (v != null) {
            setGroup(v.toString());
        }
    }

}
