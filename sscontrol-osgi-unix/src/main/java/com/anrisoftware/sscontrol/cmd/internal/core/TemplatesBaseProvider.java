package com.anrisoftware.sscontrol.cmd.internal.core;

import javax.inject.Inject;
import javax.inject.Provider;

import com.anrisoftware.resources.templates.external.Templates;
import com.anrisoftware.resources.templates.external.TemplatesFactory;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class TemplatesBaseProvider implements Provider<Templates> {

    private final Templates templates;

    @Inject
    TemplatesBaseProvider(TemplatesFactory templatesFactory) {
        this.templates = templatesFactory.create("CmdBaseTemplates");
    }

    @Override
    public Templates get() {
        return templates;
    }

}
