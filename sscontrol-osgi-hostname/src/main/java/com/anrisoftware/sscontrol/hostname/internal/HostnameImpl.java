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

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.hostname.external.Hostname;
import com.anrisoftware.sscontrol.hostname.external.HostnameService;
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

    @Inject
    private HostnameImplLogger log;

    private String hostname;

    private final List<SshHost> targets;

    @SuppressWarnings("unchecked")
    @AssistedInject
    HostnameImpl(@Assisted Map<String, Object> args) {
        this.targets = (List<SshHost>) args.get("targets");
    }

    public void set(String name) {
        this.hostname = name;
        log.hostnameSet(this, name);
    }

    @Override
    public String getHostname() {
        return hostname;
    }

    @Override
    public List<SshHost> getTargets() {
        return targets;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("hostname", hostname)
                .append("hosts", targets).toString();
    }

}
