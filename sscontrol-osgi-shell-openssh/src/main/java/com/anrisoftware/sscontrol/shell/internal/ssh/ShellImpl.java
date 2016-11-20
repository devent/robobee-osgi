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
package com.anrisoftware.sscontrol.shell.internal.ssh;

import static com.anrisoftware.sscontrol.shell.external.Cmd.ENV_ARG;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_HOST;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_KEY_ARG;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_PORT_ARG;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_USER_ARG;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SUDO_ENV_ARG;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.shell.external.Cmd;
import com.anrisoftware.sscontrol.shell.external.Shell;
import com.anrisoftware.sscontrol.shell.external.ShellExecException;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ShellImpl implements Shell {

    private static final String LOG_ARG = "log";

    private final Threads threads;

    private final Object parent;

    private final Map<String, Object> args;

    private final String command;

    private final Object log;

    private final SshHost host;

    private final Map<String, String> env;

    private final Map<String, String> sudoEnv;

    @Inject
    private Cmd cmd;

    @Inject
    ShellImpl(@Assisted Map<String, Object> args, @Assisted SshHost host,
            @Assisted("parent") Object parent, @Assisted Threads threads,
            @Assisted("log") Object log, @Assisted String command) {
        this.args = new HashMap<String, Object>(args);
        this.parent = parent;
        this.threads = threads;
        this.log = log;
        this.command = command;
        this.host = host;
        this.env = new HashMap<String, String>(getEnv("env", args));
        this.sudoEnv = new HashMap<String, String>(getEnv("sudoEnv", args));
        setupArgs();
    }

    @Override
    public ProcessTask call() throws AppException {
        try {
            return cmd.call(args, parent, threads, command);
        } catch (CommandExecException e) {
            throw new ShellExecException(e, "ssh");
        }
    }

    private void setupArgs() {
        args.put(LOG_ARG, log);
        args.put(ENV_ARG, env);
        args.put(SUDO_ENV_ARG, sudoEnv);
        args.put(SSH_USER_ARG, host.getUser());
        args.put(SSH_HOST, host.getHost());
        args.put(SSH_PORT_ARG, host.getPort());
        args.put(SSH_KEY_ARG, host.getKey());
    }

    public Shell env(String string) {
        int i = string.indexOf('=');
        String key = string.substring(0, i);
        String value = string.substring(i + 1);
        env.put(key, value);
        return this;
    }

    public Shell env(Map<String, Object> args) {
        Boolean literally = (Boolean) args.get("literally");
        String value = args.get("value").toString();
        String quote = "\'";
        if (literally != null && !literally) {
            quote = "\"";
        }
        value = String.format("%s%s%s", quote, value, quote);
        env.put(args.get("name").toString(), value);
        return this;
    }

    private Map<String, String> getEnv(String name, Map<String, Object> args) {
        @SuppressWarnings("unchecked")
        Map<String, String> env = (Map<String, String>) args.get(name);
        if (env == null) {
            env = new HashMap<String, String>();
        }
        return env;
    }

}
