package com.anrisoftware.sscontrol.hostname.debian.external;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;

import com.anrisoftware.sscontrol.types.external.HostService;
import com.anrisoftware.sscontrol.types.external.HostServiceScript;
import com.anrisoftware.sscontrol.types.external.HostServiceScriptService;
import com.anrisoftware.sscontrol.types.external.HostServices;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class Hostname_Debian_8_Service implements HostServiceScriptService {

    static final String SYSTEM_VERSION = "8";

    static final String SYSTEM_NAME = "debian";

    @Inject
    private Hostname_Debian_8_Factory hostnameFactory;

    @Override
    public String getSystemName() {
        return SYSTEM_NAME;
    }

    @Override
    public String getSystemVersion() {
        return SYSTEM_VERSION;
    }

    @Override
    public HostServiceScript create(HostServices repository,
            HostService service, SshHost target, ExecutorService threads) {
        return hostnameFactory.create(repository, service, target, threads);
    }

    @Activate
    protected void start() {
        Guice.createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                install(new FactoryModuleBuilder()
                        .implement(HostServiceScript.class,
                                Hostname_Debian_8.class)
                        .build(Hostname_Debian_8_Factory.class));
            }
        }).injectMembers(this);
    }

}
