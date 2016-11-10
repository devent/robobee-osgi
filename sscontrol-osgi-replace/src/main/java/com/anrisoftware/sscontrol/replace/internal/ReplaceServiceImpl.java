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
