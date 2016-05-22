package com.anrisoftware.sscontrol.unix.internal.core;

import javax.inject.Inject;
import javax.inject.Provider;

import com.anrisoftware.resources.templates.external.Templates;
import com.anrisoftware.resources.templates.external.TemplatesFactory;

public class UnixTemplatesProvider implements Provider<Templates> {

    private final Templates templates;

    @Inject
    UnixTemplatesProvider(TemplatesFactory templatesFactory) {
        this.templates = templatesFactory.create("UnixTemplates");
    }

    @Override
    public Templates get() {
        return templates;
    }

}
