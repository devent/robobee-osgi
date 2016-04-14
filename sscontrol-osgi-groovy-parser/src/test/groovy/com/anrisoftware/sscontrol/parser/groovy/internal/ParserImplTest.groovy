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
package com.anrisoftware.sscontrol.parser.groovy.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import org.apache.sling.testing.mock.osgi.junit.OsgiContext
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test

import com.anrisoftware.sscontrol.dhclient.internal.DhclientServiceImpl
import com.anrisoftware.sscontrol.parser.external.ParserService
import com.anrisoftware.sscontrol.types.internal.ToStringServiceImpl

@Slf4j
@CompileStatic
class ParserImplTest {

    @Rule
    public final OsgiContext context = new OsgiContext()

    @Test
    void "parse dhclient script"() {
        context.registerInjectActivateService(new ToStringServiceImpl(), null)
        context.registerInjectActivateService(new DhclientServiceImpl(), null)
        ParserService service = context.registerInjectActivateService(new ParserServiceImpl(), null)
        def parser = service.create()
        parser.parse dhclientScript
    }

    @BeforeClass
    static void setupTest() {
        toStringStyle
    }

    static final URI dhclientScript = ParserImplTest.class.getResource('DhclientScript.groovy').toURI()
}
