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
package com.anrisoftware.sscontrol.shell.internal.fetch;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.fetch.external.Fetch;
import com.anrisoftware.sscontrol.shell.external.Scp.ScpFactory;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class FetchImpl implements Fetch {

    private static final String PWD_ARG = "pwd";

    private static final String SRC_ARG = "src";

    private static final String DEST_ARG = "dest";

    private static final String LOG_ARG = "log";

    private final Map<String, Object> args;

    private final SshHost host;

    private final Object parent;

    private final Threads threads;

    private final Object log;

    @Inject
    private ScpFactory scpFactory;

    @Inject
    FetchImpl(@Assisted Map<String, Object> args, @Assisted SshHost host,
            @Assisted("parent") Object parent, @Assisted Threads threads,
            @Assisted("log") Object log) {
        this.args = new HashMap<String, Object>(args);
        this.host = host;
        this.parent = parent;
        this.threads = threads;
        this.log = log;
        checkArgs();
        setupArgs();
    }

    @Override
    public ProcessTask call() throws AppException {
        return scpFactory.create(args, host, parent, threads, log).call();
    }

    private void checkArgs() {
        isTrue(args.containsKey(SRC_ARG), "%s=null", SRC_ARG);
        notNull(args.get(SRC_ARG), "%s=null", SRC_ARG);
        isTrue(args.containsKey(PWD_ARG), "%s=null", PWD_ARG);
        notNull(args.get(PWD_ARG), "%s=null", PWD_ARG);
    }

    private void setupArgs() {
        args.put("remoteSrc", true);
        args.put("remoteDest", false);
        String src = args.get(SRC_ARG).toString();
        args.put(LOG_ARG, log);
        args.put(SRC_ARG, new File(src));
        if (args.get(DEST_ARG) == null) {
            File pwd = (File) args.get(PWD_ARG);
            args.put(DEST_ARG, pwd);
        } else {
            String dest = args.get(DEST_ARG).toString();
            args.put(DEST_ARG, new File(dest));
        }
    }

}
