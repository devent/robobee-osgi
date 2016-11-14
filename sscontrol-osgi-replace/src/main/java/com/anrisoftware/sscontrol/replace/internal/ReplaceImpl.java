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

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokensTemplate;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.sscontrol.replace.external.Replace;
import com.anrisoftware.sscontrol.replace.internal.LoadFileWorker.LoadFileWorkerFactory;
import com.anrisoftware.sscontrol.replace.internal.PushFileWorker.PushFileWorkerFactory;
import com.anrisoftware.sscontrol.replace.internal.ReplaceWorker.ReplaceWorkerFactory;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ReplaceImpl implements Replace {

    private final Map<String, Object> args;

    private final SshHost host;

    private final Object parent;

    private final Threads threads;

    private final Object log;

    @Inject
    private ReplaceWorkerFactory replace;

    @Inject
    private LoadFileWorkerFactory load;

    @Inject
    private PushFileWorkerFactory push;

    @Inject
    ReplaceImpl(@Assisted Map<String, Object> args, @Assisted SshHost host,
            @Assisted("parent") Object parent, @Assisted Threads threads,
            @Assisted("log") Object log,
            PropertiesProvider propertiesProvider) {
        this.args = new HashMap<String, Object>(args);
        this.host = host;
        this.parent = parent;
        this.threads = threads;
        this.log = log;
        setupDefaults(propertiesProvider);
        checkArgs();
    }

    @Override
    public Replace call() throws AppException {
        String text = load.create(args, host, parent, threads, log).call();
        TokensTemplate tokens = replace.create(args, text).call();
        push.create(args, host, tokens, threads, log, text).call();
        return this;
    }

    private void checkArgs() {
        isTrue(args.containsKey(DEST_ARG), "%s=null", DEST_ARG);
    }

    private void setupDefaults(PropertiesProvider propertiesProvider) {
        ContextProperties p = propertiesProvider.getProperties();
        Charset charset = (Charset) args.get(CHARSET_ARG);
        if (charset == null) {
            args.put(CHARSET_ARG, p.getCharsetProperty("default_charset"));
        }
    }

}
