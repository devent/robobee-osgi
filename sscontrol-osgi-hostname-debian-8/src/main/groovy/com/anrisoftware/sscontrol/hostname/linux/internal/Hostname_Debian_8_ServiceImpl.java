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
package com.anrisoftware.sscontrol.hostname.linux.internal;

import static com.google.inject.Guice.createInjector;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.groovy.script.external.LinuxScript;
import com.anrisoftware.sscontrol.hostname.linux.external.Hostname_Debian_8;
import com.anrisoftware.sscontrol.hostname.linux.external.Hostname_Debian_8_Service;

@Component
@Service(Hostname_Debian_8_Service.class)
public class Hostname_Debian_8_ServiceImpl
        implements Hostname_Debian_8_Service {

    @Reference
    private Hostname_Debian_8 hostnameDebian;

    @Override
    public LinuxScript getScript() {
        return hostnameDebian;
    }

    @Activate
    protected void start() {
        createInjector().injectMembers(hostnameDebian);
    }

}
