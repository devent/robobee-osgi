

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
package com.anrisoftware.sscontrol.services.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.strings.StringsModule
import com.anrisoftware.sscontrol.services.internal.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.HostnameStub.HostnameStubFactory
import com.anrisoftware.sscontrol.services.internal.HostnameStub.HostnameStubServiceImpl
import com.anrisoftware.sscontrol.services.internal.HostsStub.Host
import com.anrisoftware.sscontrol.services.internal.HostsStub.HostsStubFactory
import com.anrisoftware.sscontrol.services.internal.HostsStub.HostsStubServiceImpl
import com.anrisoftware.sscontrol.services.internal.SshStub.SshStubFactory
import com.anrisoftware.sscontrol.services.internal.SshStub.SshStubServiceImpl
import com.anrisoftware.sscontrol.services.internal.TargetsImpl.TargetsImplFactory
import com.anrisoftware.sscontrol.types.external.HostServices
import com.anrisoftware.sscontrol.types.external.TargetsService
import com.anrisoftware.sscontrol.types.internal.TypesModule
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.assistedinject.FactoryModuleBuilder

/**
 * 
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@CompileStatic
class HostServicesImplTest {

    Injector injector

    @Inject
    HostServicesImplFactory servicesFactory

    @Inject
    HostnameStubServiceImpl hostnameService

    @Inject
    SshStubServiceImpl sshService

    @Inject
    HostsStubServiceImpl hostsService

    @Test
    void "load mock service"() {
        def testCases = [
            [
                input: '''
service 'hostname' with {
    // Sets the hostname.
    set "blog.muellerpublic.de"
}
''',
                expected: { HostServices services ->
                    assert services.getServices('hostname').size() == 1
                    HostnameStub service = services.getServices('hostname')[0] as HostnameStub
                    assert service.name == 'blog.muellerpublic.de'
                },
            ],
            [
                input: '''
service 'ssh' with {
    group "nodes"
    host "192.168.0.3"
    host "192.168.0.4"
    host "192.168.0.5"
}

service 'hosts', target: 'nodes' with {
    targets.eachWithIndex { h, i ->
        host "node-${i}.muellerpublic.de", "$h.host"
    }
}
''',
                expected: { HostServices services ->
                    assert services.getServices('hosts').size() == 1
                    int i = 0
                    HostsStub service = services.getServices('hosts')[i++] as HostsStub
                    int k = 0
                    Host host = service.hosts[k++] as Host
                    assert host.host == 'node-0.muellerpublic.de'
                    host = service.hosts[k++] as Host
                    assert host.host == 'node-1.muellerpublic.de'
                    host = service.hosts[k++] as Host
                    assert host.host == 'node-2.muellerpublic.de'
                },
            ],
            [
                input: '''
service 'ssh' with {
    group "nodes"
    host "192.168.0.3"
    host "192.168.0.4"
    host "192.168.0.5"
}

targets 'nodes' eachWithIndex { host, i ->
    service 'hostname' with {
        set "node-${i}.muellerpublic.de"
    }
}
''',
                expected: { HostServices services ->
                    assert services.getServices('hostname').size() == 3
                    int i = 0
                    HostnameStub service = services.getServices('hostname')[i++] as HostnameStub
                    assert service.name == 'node-0.muellerpublic.de'
                    service = services.getServices('hostname')[i++] as HostnameStub
                    assert service.name == 'node-1.muellerpublic.de'
                    service = services.getServices('hostname')[i++] as HostnameStub
                    assert service.name == 'node-2.muellerpublic.de'
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            log.info '{}. case: {}', k, test
            def services = servicesFactory.create()
            def targets = services.getTargets()
            services.putAvailableService 'hostname', hostnameService
            services.putAvailableService 'ssh', sshService
            services.putAvailableService 'hosts', hostsService
            eval service: services, targets: targets, test.input as String
            Closure expected = test.expected
            expected services
        }
    }

    def eval(Map args, String script) {
        def b = new Binding()
        args.each { name, value ->
            b.setVariable name as String, value
        }
        def sh = new GroovyShell(b)
        sh.evaluate script
    }

    @Before
    void setupTest() {
        toStringStyle
        this.injector = Guice.createInjector(
                new HostServicesModule(),
                new TargetsModule(),
                new TypesModule(),
                new StringsModule(),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(TargetsService).to(TargetsImplFactory)
                        install(new FactoryModuleBuilder().implement(HostnameStub, HostnameStub).build(HostnameStubFactory));
                        install(new FactoryModuleBuilder().implement(SshStub, SshStub).build(SshStubFactory));
                        install(new FactoryModuleBuilder().implement(HostsStub, HostsStub).build(HostsStubFactory));
                    }
                })
        injector.injectMembers(this)
    }
}
