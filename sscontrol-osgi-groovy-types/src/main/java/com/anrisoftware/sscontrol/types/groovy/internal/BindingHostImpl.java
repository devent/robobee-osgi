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
package com.anrisoftware.sscontrol.types.groovy.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.BindingAddress;
import com.anrisoftware.sscontrol.types.external.BindingHost;
import com.anrisoftware.sscontrol.types.external.ToStringService;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Binding host and port.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class BindingHostImpl implements BindingHost {

    public interface BindingHostImplFactory {

        BindingHostImpl create();

        BindingHostImpl create(@Assisted BindingHost binding);

    }

    private final List<Host> addedHosts;

    private final List<Host> removedHosts;

    @Inject
    private BindingHostImplLogger log;

    @Inject
    private ToStringService toString;

    @AssistedInject
    BindingHostImpl() {
        this.addedHosts = new ArrayList<Host>();
        this.removedHosts = new ArrayList<Host>();
    }

    @AssistedInject
    BindingHostImpl(@Assisted BindingHost binding) {
        this.addedHosts = new ArrayList<Host>(binding.getAddedHosts());
        this.removedHosts = new ArrayList<Host>(binding.getRemovedHosts());
    }

    public void binding(Map<String, Object> args, BindingAddress address)
            throws AppException {
        binding(args, address.getAddress());
    }

    public void binding(Map<String, Object> args, String host)
            throws AppException {
        Map<String, Object> a = new HashMap<String, Object>(args);
        a.put("host", host);
        binding(a);
    }

    public void binding(Map<String, Object> args) throws AppException {
        Number port = (Number) args.get("port");
        Integer p = port != null ? port.intValue() : null;
        String host = toString.toString(args, "host");
        if (StringUtils.startsWith(host, "!")) {
            host = host.substring(1);
            Host h = new Host(host, p);
            removedHosts.add(h);
            log.hostRemoveAdded(this, h);
        } else {
            Host h = new Host(host, p);
            addedHosts.add(h);
            log.hostAddAdded(this, h);
        }
    }

    @Override
    public List<Host> getAddedHosts() {
        return Collections.unmodifiableList(addedHosts);
    }

    @Override
    public List<Host> getRemovedHosts() {
        return Collections.unmodifiableList(removedHosts);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("add", addedHosts)
                .append("remove", removedHosts).toString();
    }
}
