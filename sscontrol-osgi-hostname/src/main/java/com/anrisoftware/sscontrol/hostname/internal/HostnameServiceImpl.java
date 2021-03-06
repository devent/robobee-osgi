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

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.hostname.external.Hostname;
import com.anrisoftware.sscontrol.hostname.external.HostnameService;
import com.anrisoftware.sscontrol.hostname.internal.HostnameImpl.HostnameImplFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

/**
 * Creates the hostname service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(HostnameService.class)
public class HostnameServiceImpl implements HostnameService {

    static final String HOSTNAME_NAME = "hostname";

    @Inject
    private HostnameImplFactory hostnameFactory;

    @Override
    public Hostname create(Map<String, Object> args) {
        return (Hostname) hostnameFactory.create(args);
    }

    @Activate
    protected void start() {
        Guice.createInjector(new HostnameModule(), new AbstractModule() {

            @Override
            protected void configure() {
            }
        }).injectMembers(this);
    }
}
