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

import static org.codehaus.groovy.runtime.InvokerHelper.invokeMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.debug.external.DebugService;
import com.anrisoftware.sscontrol.ssh.internal.SshHostImpl.SshHostImplFactory;
import com.anrisoftware.sscontrol.types.external.DebugLogging;
import com.anrisoftware.sscontrol.types.external.Ssh;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Ssh</i> script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class SshImpl implements Ssh {

    public interface SshImplFactory {

        SshImpl create();

        SshImpl create(@Assisted Ssh ssh);

    }

    private final List<SshHost> hosts;

    @Inject
    private SshImplLogger log;

    @Inject
    private SshHostImplFactory sshHostFactory;

    private DebugLogging debug;

    @AssistedInject
    SshImpl() {
        this.hosts = new ArrayList<SshHost>();
    }

    @AssistedInject
    SshImpl(@Assisted Ssh ssh) {
        this.hosts = new ArrayList<SshHost>(ssh.getHosts());
    }

    @Inject
    public void setDebugService(DebugService debugService) {
        this.debug = debugService.create();
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

    @SuppressWarnings("serial")
    public List<String> getHost() {
        return new ArrayList<String>() {
            @Override
            public boolean add(String host) {
                host(host);
                return super.add(host);
            }
        };
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
    public List<SshHost> getHosts() {
        return Collections.unmodifiableList(hosts);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }

}
