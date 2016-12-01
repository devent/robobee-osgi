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
package com.anrisoftware.sscontrol.hosts.linux.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenMarker
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokensTemplateFactory
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokensTemplateModule
import com.google.inject.Guice

import groovy.util.logging.Slf4j

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class Hosts_Replace_Test {

    @Test
    void "replace hosts"() {
        def search = "^.*(srv1.ubuntutest.com).*"
        def replace = "192.168.0.52 srv1.ubuntutest.com "
        def tokens = new TokenMarker("#ROBOBEE_BEGIN", "#ROBOBEE_END\n")
        def text =
                """127.0.0.1       localhost
127.0.1.1       master.muellerpublic.de master

# The following lines are desirable for IPv6 capable hosts
::1     localhost ip6-localhost ip6-loopback
ff02::1 ip6-allnodes
ff02::2 ip6-allrouters
"""
        def template = new TokenTemplate(search, replace)
        def worker = factory.create tokens, template, text
        worker.replace()
        assertStringContent worker.text,
                """127.0.0.1       localhost
127.0.1.1       master.muellerpublic.de master

# The following lines are desirable for IPv6 capable hosts
::1     localhost ip6-localhost ip6-loopback
ff02::1 ip6-allnodes
ff02::2 ip6-allrouters
#ROBOBEE_BEGIN
192.168.0.52 srv1.ubuntutest.com 
#ROBOBEE_END
"""
        //
        search = "^.*(srv1.ubuntutest.de).*"
        replace = "192.168.0.49 srv1.ubuntutest.de srv1"
        tokens = new TokenMarker("#ROBOBEE_BEGIN", "#ROBOBEE_END\n")
        template = new TokenTemplate(search, replace)
        text = worker.text
        worker = factory.create tokens, template, text
        worker.replace()
        assertStringContent worker.text,
                """127.0.0.1       localhost
127.0.1.1       master.muellerpublic.de master

# The following lines are desirable for IPv6 capable hosts
::1     localhost ip6-localhost ip6-loopback
ff02::1 ip6-allnodes
ff02::2 ip6-allrouters
#ROBOBEE_BEGIN
192.168.0.52 srv1.ubuntutest.com 
#ROBOBEE_END
#ROBOBEE_BEGIN
192.168.0.49 srv1.ubuntutest.de srv1
#ROBOBEE_END
"""
    }

    @Inject
    TokensTemplateFactory factory

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(new TokensTemplateModule()).injectMembers(this)
    }
}
