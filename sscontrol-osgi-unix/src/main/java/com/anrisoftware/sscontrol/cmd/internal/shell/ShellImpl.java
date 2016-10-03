package com.anrisoftware.sscontrol.cmd.internal.shell;

import static com.anrisoftware.sscontrol.cmd.external.Cmd.ENV_ARGS;
import static com.anrisoftware.sscontrol.cmd.external.Cmd.SSH_HOST;
import static com.anrisoftware.sscontrol.cmd.external.Cmd.SSH_PORT;
import static com.anrisoftware.sscontrol.cmd.external.Cmd.SSH_USER;
import static com.anrisoftware.sscontrol.cmd.external.Cmd.SUDO_ENV_ARGS;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.cmd.external.Cmd;
import com.anrisoftware.sscontrol.cmd.external.Shell;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ShellImpl implements Shell {

    private final Threads threads;

    private final Object parent;

    private final Map<String, Object> args;

    private final String command;

    private final Object log;

    private final SshHost ssh;

    private final Map<String, String> env;

    private final Map<String, String> sudoEnv;

    @Inject
    private Cmd cmd;

    @Inject
    ShellImpl(@Assisted Map<String, Object> args, @Assisted SshHost ssh,
            @Assisted("parent") Object parent, @Assisted Threads threads,
            @Assisted("log") Object log, @Assisted String command) {
        this.args = new HashMap<String, Object>(args);
        this.parent = parent;
        this.threads = threads;
        this.log = log;
        this.command = command;
        this.ssh = ssh;
        this.env = new HashMap<String, String>(getEnv("env", args));
        this.sudoEnv = new HashMap<String, String>(getEnv("sudoEnv", args));
    }

    @Override
    public ProcessTask call() throws CommandExecException {
        args.put("log", log);
        args.put(ENV_ARGS, env);
        args.put(SUDO_ENV_ARGS, sudoEnv);
        setupSsh();
        return cmd.call(args, parent, threads, command);
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

    private void setupSsh() {
        args.put(SSH_USER, ssh.getUser());
        args.put(SSH_HOST, ssh.getHost());
        args.put(SSH_PORT, ssh.getPort());
    }
}
