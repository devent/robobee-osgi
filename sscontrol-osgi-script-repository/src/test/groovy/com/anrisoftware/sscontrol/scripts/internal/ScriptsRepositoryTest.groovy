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
package com.anrisoftware.sscontrol.scripts.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.dhclient.external.DhclientService
import com.anrisoftware.sscontrol.dhclient.internal.DhclientModule
import com.anrisoftware.sscontrol.dhclient.internal.DhclientServiceImpl
import com.anrisoftware.sscontrol.types.internal.TypesModule
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector

@Slf4j
@CompileStatic
class ScriptsRepositoryTest {

    Injector injector

    @Test
    void "load dhclient script"() {
        log.info '{}', dhclientScript
    }

    @Before
    void setupTest() {
        toStringStyle
        this.injector = Guice.createInjector(
                new ScriptsRepositoryModule(),
                new DhclientModule(),
                new TypesModule(),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind DhclientService to DhclientServiceImpl
                    }
                })
        injector.injectMembers(this)
    }

    static final URI dhclientScript = ScriptsRepositoryTest.class.getResource('DhclientScript.groovy').toURI()
}
