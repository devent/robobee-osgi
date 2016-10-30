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
package com.anrisoftware.sscontrol.replace.internal;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenMarker;
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate;
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokensTemplateFactory;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.sscontrol.replace.external.Replace;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ReplaceImpl implements Replace {

    private final Map<String, Object> args;

    @Inject
    private TokensTemplateFactory tokensTemplateFactory;

    @Inject
    private PropertiesProvider propertiesProvider;

    @Inject
    ReplaceImpl(@Assisted Map<String, Object> args) {
        this.args = new HashMap<>(args);
    }

    @Override
    public Replace call() throws AppException {
        setupDefaults();
        String beginToken = args.get("beginToken").toString();
        String endToken = args.get("endToken").toString();
        TokenMarker tokens = new TokenMarker(beginToken, endToken);
        String search = args.get("search").toString();
        String replace = args.get("replace").toString();
        TokenTemplate template = new TokenTemplate(args, search, replace);
    }

    private void setupDefaults() {
        ContextProperties p = propertiesProvider.getProperties();
        if (!args.containsKey("beginToken")) {
            args.put("beginToken", p.getProperty("default_begin_token"));
        }
        if (!args.containsKey("endToken")) {
            args.put("endToken", p.getProperty("default_end_token"));
        }
    }

}
