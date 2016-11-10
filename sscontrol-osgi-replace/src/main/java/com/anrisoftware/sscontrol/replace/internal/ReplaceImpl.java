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

import static org.apache.commons.lang3.Validate.isTrue;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;

import com.anrisoftware.globalpom.resources.ToURL;
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenMarker;
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate;
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokensTemplate;
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokensTemplateFactory;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.sscontrol.replace.external.LoadFileException;
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

    private static final String CHARSET_ARG = "charset";

    private static final String REPLACE_ARG = "replace";

    private static final String SEARCH_ARG = "search";

    private static final String END_TOKEN_ARG = "endToken";

    private static final String BEGIN_TOKEN_ARG = "beginToken";

    private static final String DEST_ARG = "dest";

    private final Map<String, Object> args;

    @Inject
    private TokensTemplateFactory tokensTemplateFactory;

    @Inject
    private PropertiesProvider propertiesProvider;

    @Inject
    ReplaceImpl(@Assisted Map<String, Object> args) {
        this.args = new HashMap<>(args);
        setupDefaults(this.args);
        checkArgs(this.args);
    }

    @Override
    public Replace call() throws AppException {
        String beginToken = args.get(BEGIN_TOKEN_ARG).toString();
        String endToken = args.get(END_TOKEN_ARG).toString();
        URL dest = ToURL.toURL(args.get(DEST_ARG).toString());
        Charset charset = (Charset) args.get(CHARSET_ARG);
        String text = loadFile(dest, charset);
        TokenMarker tokens = new TokenMarker(beginToken, endToken);
        String search = args.get(SEARCH_ARG).toString();
        String replace = args.get(REPLACE_ARG).toString();
        TokenTemplate template = new TokenTemplate(args, search, replace);
        TokensTemplate worker = tokensTemplateFactory
                .create(tokens, template, text).replace();
    }

    private String loadFile(URL dest, Charset charset)
            throws LoadFileException {
        try {
            return IOUtils.toString(dest, charset);
        } catch (IOException e) {
            throw new LoadFileException(dest, charset, e);
        }
    }

    private void checkArgs(Map<String, Object> args) {
        isTrue(args.containsKey(REPLACE_ARG), "%s==null", REPLACE_ARG);
        isTrue(args.containsKey(SEARCH_ARG), "%s==null", SEARCH_ARG);
        isTrue(args.containsKey(DEST_ARG), "%s==null", DEST_ARG);
    }

    private void setupDefaults(Map<String, Object> args) {
        ContextProperties p = propertiesProvider.getProperties();
        if (!args.containsKey(BEGIN_TOKEN_ARG)) {
            args.put(BEGIN_TOKEN_ARG, p.getProperty("default_begin_token"));
        }
        if (!args.containsKey(END_TOKEN_ARG)) {
            args.put(END_TOKEN_ARG, p.getProperty("default_end_token"));
        }
        if (!args.containsKey(CHARSET_ARG)) {
            args.put(CHARSET_ARG, Charset.defaultCharset());
        }
    }

}
