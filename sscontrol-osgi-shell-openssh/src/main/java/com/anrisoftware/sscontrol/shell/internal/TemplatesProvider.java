/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-shell-openssh.
 *
 * sscontrol-osgi-shell-openssh is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-shell-openssh is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-shell-openssh. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.shell.internal;

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
