package com.anrisoftware.sscontrol.cmd.internal.core;

import static com.anrisoftware.sscontrol.cmd.external.Cmd.SHELL;
import static com.anrisoftware.sscontrol.cmd.external.Cmd.SSH_ARGS;
import static com.anrisoftware.sscontrol.cmd.external.Cmd.SSH_CONNECTION_TIMEOUT;
import static com.anrisoftware.sscontrol.cmd.external.Cmd.SSH_CONTROL_MASTER;
import static com.anrisoftware.sscontrol.cmd.external.Cmd.SSH_CONTROL_PATH;
import static com.anrisoftware.sscontrol.cmd.external.Cmd.SSH_CONTROL_PERSIST_DURATION;
import static com.anrisoftware.sscontrol.cmd.external.Cmd.SSH_PORT;
import static com.anrisoftware.sscontrol.cmd.external.Cmd.SSH_USER;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.durationformat.DurationFormatFactory;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.sscontrol.cmd.external.core.ParsePropertiesErrorException;
import com.anrisoftware.sscontrol.cmd.internal.core.ArgsMap.ArgsMapFactory;
import com.anrisoftware.sscontrol.cmd.internal.core.SshOptions.SshOptionsFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class CmdArgs {

    interface CmdArgsFactory {

        CmdArgs create(Map<String, Object> args);

    }

    private final ArgsMap args;

    @Inject
    private PropertiesProvider propertiesProvider;

    @Inject
    private DurationFormatFactory durationFormatFactory;

    @Inject
    private SshOptionsFactory sshOptionsFactory;

    @Inject
    CmdArgs(@Assisted Map<String, Object> args, ArgsMapFactory argsMapFactory) {
        this.args = argsMapFactory.create(new HashMap<String, Object>(args));
    }

    public ArgsMap getArgs() {
        setupDefaults();
        setupSshDefaultArgs();
        return args;
    }

    private void setupDefaults() {
        Object arg = args.get(SHELL);
        ContextProperties p = propertiesProvider.get().withSystemReplacements();
        if (arg == null) {
            String shell = p.getProperty("default_shell");
            args.put(SHELL, shell);
        }
        arg = args.get(SSH_USER);
        if (arg == null) {
            args.put(SSH_USER, p.getProperty("default_ssh_user"));
        }
        arg = args.get(SSH_PORT);
        if (arg == null) {
            args.put(SSH_PORT,
                    p.getNumberProperty("default_ssh_port").intValue());
        }
        arg = args.get(SSH_ARGS);
        if (arg == null) {
            args.put(SSH_ARGS, p.getListProperty("default_ssh_args", ";"));
        }
        arg = args.get(SSH_CONTROL_MASTER);
        if (arg == null) {
            args.put(SSH_CONTROL_MASTER,
                    p.getProperty("default_ssh_control_master"));
        }
        arg = args.get(SSH_CONTROL_PERSIST_DURATION);
        if (arg == null) {
            args.put(SSH_CONTROL_PERSIST_DURATION, getDefaultDuration(p,
                    "default_ssh_control_persist_duration"));
        }
        arg = args.get(SSH_CONTROL_PATH);
        if (arg == null) {
            args.put(SSH_CONTROL_PATH,
                    p.getProperty("default_ssh_control_path"));
        }
        arg = args.get(SSH_CONNECTION_TIMEOUT);
        if (arg == null) {
            args.put(SSH_CONNECTION_TIMEOUT,
                    getDefaultDuration(p, "default_ssh_connect_timeout"));
        }
    }

    private void setupSshDefaultArgs() {
        List<String> options = new ArrayList<String>();
        args.put("sshDefaultOptions", options);
        SshOptions sshOptions = sshOptionsFactory.create(args, options);
        sshOptions.addDefaultOptions();
        sshOptions.addDebug();
        sshOptions.addStringOption(SSH_CONTROL_MASTER,
                "ssh_control_master_option");
        sshOptions.addOption(SSH_CONTROL_PERSIST_DURATION,
                "ssh_control_persist_option");
        sshOptions.addOption(SSH_CONNECTION_TIMEOUT,
                "ssh_connection_timeout_option");
        sshOptions.addPathOption(SSH_CONTROL_PATH, "ssh_control_path_option");
    }

    private Object getDefaultDuration(ContextProperties p, String property) {
        try {
            return p.getTypedProperty(property, durationFormatFactory.create());
        } catch (ParseException e) {
            throw new ParsePropertiesErrorException(e, property);
        }
    }

}
