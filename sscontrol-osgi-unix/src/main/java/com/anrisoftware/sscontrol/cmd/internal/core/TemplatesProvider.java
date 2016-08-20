package com.anrisoftware.sscontrol.cmd.internal.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import org.stringtemplate.v4.STGroup;

import com.anrisoftware.resources.templates.external.SerializiableGroup;
import com.anrisoftware.resources.templates.external.Templates;
import com.anrisoftware.resources.templates.external.TemplatesFactory;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class TemplatesProvider implements Provider<Templates> {

    private final Templates templates;

    @Inject
    TemplatesProvider(TemplatesBaseProvider baseProvider,
            TemplatesFactory templatesFactory) {
        Map<Serializable, Serializable> attr = new HashMap<Serializable, Serializable>();
        ArrayList<Serializable> parents = new ArrayList<Serializable>();
        parents.add(new SerializiableGroup((STGroup) baseProvider.get()
                .getResource("ssh_base").getTemplate()));
        attr.put("imports", parents);
        this.templates = templatesFactory.create("CmdTemplates", attr);
    }

    @Override
    public Templates get() {
        return templates;
    }

}
