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

import static java.util.Collections.synchronizedMap;
import static java.util.Collections.unmodifiableMap;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.types.external.Ssh;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.anrisoftware.sscontrol.types.external.Targets;
import com.anrisoftware.sscontrol.types.external.TargetsService;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Host services repository.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class TargetsImpl implements Targets {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface TargetsImplFactory extends TargetsService {

    }

    private final Map<String, List<Ssh>> hosts;

    @Inject
    private TargetsImplLogger log;

    @AssistedInject
    TargetsImpl() {
        this.hosts = synchronizedMap(new HashMap<String, List<Ssh>>());
    }

    public List<SshHost> call(String name) {
        return call(new HashMap<String, Object>(), name);
    }

    public List<SshHost> call(Map<String, Object> args, String name) {
        List<SshHost> hosts = getHosts(name);
        parseArgs(args);
        return hosts;
    }

    @Override
    public List<SshHost> getHosts(String name) {
        List<Ssh> targets = hosts.get(name);
        List<SshHost> result = new ArrayList<SshHost>();
        for (Ssh ssh : targets) {
            result.addAll(ssh.getHosts());
        }
        return result;
    }

    @Override
    public Set<String> getGroups() {
        return hosts.keySet();
    }

    @Override
    public void addTarget(Ssh ssh) {
        String group = ssh.getGroup();
        if (isBlank(group)) {
            group = "default";
        }
        List<Ssh> list = hosts.get(group);
        if (list == null) {
            list = new ArrayList<Ssh>();
            hosts.put(group, list);
        }
        list.add(ssh);
        log.addHosts(this, ssh, group);
    }

    @Override
    public void removeTarget(String name) {
        if (isBlank(name)) {
            name = "default";
        }
        hosts.remove(name);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("hosts", getGroups())
                .toString();
    }

    private Map<String, Object> parseArgs(Map<String, Object> args) {
        Map<String, Object> result = new HashMap<String, Object>();
        return unmodifiableMap(result);
    }

}
