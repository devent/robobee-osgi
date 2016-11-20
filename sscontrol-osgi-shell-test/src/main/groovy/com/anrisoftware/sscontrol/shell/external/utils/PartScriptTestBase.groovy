package com.anrisoftware.sscontrol.shell.external.utils

import static com.anrisoftware.globalpom.utils.TestUtils.toStringStyle
import groovy.util.logging.Slf4j

import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesImpl.HostServicePropertiesImplFactory
import com.anrisoftware.sscontrol.types.external.HostPropertiesService
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
abstract class PartScriptTestBase {

    Injector injector

    void doTest(Map test, File tmp, int k) {
        test.args["pwd"] = tmp
        test.args["chdir"] = tmp
        test.args["env"] = [:]
        test.args["env"]["PATH"] = "./"
        test.args["sudoEnv"] = [:]
        test.args["sudoEnv"]["PATH"] = "./"
        test.args["sudoChdir"] = tmp
        def host = SshFactory.localhost(injector).hosts[0]
        def parent = this
        def cmd = createCmd test, tmp, k
        cmd()
        Closure expected = test.expected
        expected([name: test.name, cmd: cmd, dir: tmp])
    }

    abstract Module[] getAdditionalModules()

    abstract def createCmd(Map test, File tmp, int k)

    void setupTest() {
        toStringStyle
        this.injector = Guice.createInjector(additionalModules).createChildInjector(
                new PropertiesModule(),
                new PropertiesUtilsModule(),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(HostPropertiesService).to(HostServicePropertiesImplFactory)
                    }
                }
                )
        this.injector.injectMembers(this)
    }
}
