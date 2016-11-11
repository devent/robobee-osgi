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
package com.anrisoftware.sscontrol.hostname.debian_8.internal

import static com.anrisoftware.sscontrol.hostname.debian_8.internal.Hostname_Debian_8_Service.*
import static com.google.inject.Guice.createInjector

import javax.inject.Inject

import org.apache.felix.scr.annotations.Activate
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Service

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.hostname.debian.external.Hostname_Debian

import groovy.util.logging.Slf4j

/**
 * Configures the <i>hostname</i> on Debian 8 systems.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
@Component
@Service(Hostname_Debian_8.class)
class Hostname_Debian_8 extends Hostname_Debian {

    @Inject
    Hostname_Debian_8_Properties debianPropertiesProvider

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Activate
    void start() {
        createInjector().injectMembers(this)
    }

    @Override
    def getLog() {
        log
    }

    @Override
    String getSystemName() {
        SYSTEM_NAME
    }

    @Override
    String getSystemVersion() {
        SYSTEM_VERSION
    }
}
