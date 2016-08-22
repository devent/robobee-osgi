package com.anrisoftware.sscontrol.cmd.internal.shell;

import com.anrisoftware.sscontrol.cmd.external.Shell;
import com.anrisoftware.sscontrol.cmd.external.Shell.ShellFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ShellModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(Shell.class, ShellImpl.class)
                .build(ShellFactory.class));
    }

}
