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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

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
public class TargetsImpl implements Targets, Map<String, List<SshHost>> {

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
        List<SshHost> result = new ArrayList<>();
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
            list = new ArrayList<>();
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

    @Override
    public int size() {
        return hosts.size();
    }

    @Override
    public boolean isEmpty() {
        return hosts.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return hosts.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return hosts.containsValue(value);
    }

    @Override
    public List<SshHost> get(Object key) {
        return getHosts(key.toString());
    }

    @Override
    public List<SshHost> put(String key, List<SshHost> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SshHost> remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends String, ? extends List<SshHost>> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> keySet() {
        return getGroups();
    }

    @Override
    public Collection<List<SshHost>> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<java.util.Map.Entry<String, List<SshHost>>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        return hosts.equals(o);
    }

    @Override
    public int hashCode() {
        return hosts.hashCode();
    }

    @Override
    public List<SshHost> getOrDefault(Object key, List<SshHost> defaultValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEach(
            BiConsumer<? super String, ? super List<SshHost>> action) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceAll(
            BiFunction<? super String, ? super List<SshHost>, ? extends List<SshHost>> function) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SshHost> putIfAbsent(String key, List<SshHost> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean replace(String key, List<SshHost> oldValue,
            List<SshHost> newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SshHost> replace(String key, List<SshHost> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SshHost> computeIfAbsent(String key,
            Function<? super String, ? extends List<SshHost>> mappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SshHost> computeIfPresent(String key,
            BiFunction<? super String, ? super List<SshHost>, ? extends List<SshHost>> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SshHost> compute(String key,
            BiFunction<? super String, ? super List<SshHost>, ? extends List<SshHost>> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SshHost> merge(String key, List<SshHost> value,
            BiFunction<? super List<SshHost>, ? super List<SshHost>, ? extends List<SshHost>> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    private Map<String, Object> parseArgs(Map<String, Object> args) {
        Map<String, Object> result = new HashMap<>();
        return unmodifiableMap(result);
    }

}
