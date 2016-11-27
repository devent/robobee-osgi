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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokensTemplate;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.sscontrol.replace.external.Replace;
import com.anrisoftware.sscontrol.replace.internal.LoadFileWorker.LoadFileWorkerFactory;
import com.anrisoftware.sscontrol.replace.internal.PushFileWorker.PushFileWorkerFactory;
import com.anrisoftware.sscontrol.replace.internal.ReplaceLine.ReplaceLineFactory;
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

    private final List<ReplaceLine> lines;

    private final SshHost host;

    private final Object parent;

    private final Threads threads;

    private final Object cmdLog;

    @Inject
    private ReplaceImplLogger log;

    @Inject
    private ReplaceWorkerFactory replace;

    @Inject
    private LoadFileWorkerFactory load;

    @Inject
    private PushFileWorkerFactory push;

    @Inject
    private ReplaceLineFactory lineFactory;

    @Inject
    ReplaceImpl(@Assisted Map<String, Object> args, @Assisted SshHost host,
            @Assisted("parent") Object parent, @Assisted Threads threads,
            @Assisted("log") Object cmdLog,
            PropertiesProvider propertiesProvider) {
        this.args = new HashMap<String, Object>(args);
        this.lines = new ArrayList<ReplaceLine>();
        this.host = host;
        this.parent = parent;
        this.threads = threads;
        this.cmdLog = cmdLog;
        setupDefaults(propertiesProvider);
        checkArgs();
    }

    public void line(String replace) {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("replace", replace);
        line(args);
    }

    public void line(Map<String, Object> args) {
        ReplaceLine line = lineFactory.create(args);
        log.lineAdded(this, line);
        lines.add(line);
    }

    @Override
    public Replace call() throws AppException {
        String text = load.create(args, host, parent, threads, cmdLog).call();
        if (lines.size() == 0) {
            lines.add(lineFactory.create(args));
        }
        text = setupReplaceLines(text);
        push.create(args, host, parent, threads, cmdLog, text).call();
        return this;
    }

    private String setupReplaceLines(String text) throws AppException {
        for (ReplaceLine line : lines) {
            Map<String, Object> a = line.getArgs(args);
            TokensTemplate tokens = replace.create(a, text).call();
            text = tokens.getText();
        }
        return text;
    }

    private void checkArgs() {
        isTrue(args.containsKey(DEST_ARG), "%s=null", DEST_ARG);
    }

    private void setupDefaults(PropertiesProvider propertiesProvider) {
        ContextProperties p = propertiesProvider.getProperties();
        args.put("basepath", true);
        Object arg = args.get(CHARSET_ARG);
        if (arg == null) {
            args.put(CHARSET_ARG, p.getCharsetProperty("default_charset"));
        }
    }

}
