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
package com.anrisoftware.sscontrol.dhclient.internal;

import static com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.stringListStatement;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.sscontrol.dhclient.external.DeclareStatement;
import com.anrisoftware.sscontrol.dhclient.external.Dhclient;
import com.anrisoftware.sscontrol.dhclient.external.DhclientService;
import com.anrisoftware.sscontrol.dhclient.internal.DeclareStatementImpl.DeclareStatementImplFactory;
import com.anrisoftware.sscontrol.types.external.HostPropertiesService;
import com.anrisoftware.sscontrol.types.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>dhclient</i> script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class DhclientImpl extends AbstractDeclaration implements Dhclient {

    public interface DhclientImplFactory extends DhclientService {

    }

    private final List<SshHost> targets;

    private final HostServiceProperties serviceProperties;

    @Inject
    private DeclareStatementImplFactory declareFactory;

    @SuppressWarnings("unchecked")
    @AssistedInject
    DhclientImpl(HostPropertiesService propertiesService,
            @Assisted Map<String, Object> args) {
        super();
        this.targets = (List<SshHost>) args.get("targets");
        this.serviceProperties = propertiesService.create();
    }

    public DeclareStatement declare(String name) {
        DeclareStatementImpl s = declareFactory.create();
        InvokerHelper.invokeMethod(s, "declare", name);
        addStatement(s);
        return s;
    }

    public DeclareStatement declare(String name, String value) {
        DeclareStatementImpl s = declareFactory.create();
        InvokerHelper.invokeMethod(s, "declare", new Object[] { name, value });
        addStatement(s);
        return s;
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
    public SshHost getTarget() {
        return getTargets().get(0);
    }

    @Override
    public List<SshHost> getTargets() {
        return targets;
    }

    @Override
    public HostServiceProperties getServiceProperties() {
        return serviceProperties;
    }

    @Override
    public String getName() {
        return "dhclient";
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("targets", targets).appendSuper(super.toString())
                .toString();
    }

}
