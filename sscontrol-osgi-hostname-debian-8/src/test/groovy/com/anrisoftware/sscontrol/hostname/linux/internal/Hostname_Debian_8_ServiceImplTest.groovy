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
package com.anrisoftware.sscontrol.hostname.linux.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import org.apache.sling.testing.mock.osgi.junit.OsgiContext
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.sscontrol.hostname.linux.external.Hostname_Debian_8
import com.anrisoftware.sscontrol.hostname.linux.external.Hostname_Debian_8_Service

/**
 * @see PropertiesThreadsServiceImpl
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 3.1
 */
@Slf4j
class Hostname_Debian_8_ServiceImplTest {

    @Test
    void "hostname service"() {
        def script = hostnameService.script
        println script
        println script.ubuntuProperties
    }

    @Rule
    public final OsgiContext context = new OsgiContext()
    
    Hostname_Debian_8_Service hostnameService

    @Before
    void createFactories() {
        context.registerInjectActivateService(new Hostname_Debian_8(), null)
        this.hostnameService = context.registerInjectActivateService(new Hostname_Debian_8_ServiceImpl(), null)
    }

    @BeforeClass
    static void setupThreads() {
        toStringStyle
        //properties = new ContextPropertiesFactory(PropertiesThreadsImpl).fromResource(propertiesResource)
    }
}
