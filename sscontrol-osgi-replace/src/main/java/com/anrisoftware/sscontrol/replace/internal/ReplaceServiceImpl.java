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

import static com.google.inject.util.Providers.of;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokensTemplateService;
import com.anrisoftware.sscontrol.replace.external.Replace;
import com.anrisoftware.sscontrol.replace.external.Replace.ReplaceFactory;
import com.anrisoftware.sscontrol.replace.external.ReplaceService;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

/**
 * Replace command service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(ReplaceService.class)
public class ReplaceServiceImpl implements ReplaceService {

    @Inject
    private ReplaceFactory shellFactory;

    @Reference
    private TokensTemplateService tokensTemplateService;

    @Override
    public Replace create(Map<String, Object> args) {
        return shellFactory.create(args);
    }

    @Activate
    protected void start() {
        Guice.createInjector(new ReplaceModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(TokensTemplateService.class)
                        .toProvider(of(tokensTemplateService));
            }
        }).injectMembers(this);
    }
}
