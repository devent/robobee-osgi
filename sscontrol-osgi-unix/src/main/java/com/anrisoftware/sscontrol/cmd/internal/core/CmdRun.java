package com.anrisoftware.sscontrol.cmd.internal.core;

import static com.anrisoftware.sscontrol.cmd.external.Cmd.SHELL;
import static com.anrisoftware.sscontrol.cmd.external.Cmd.SSH_ARGS;
import static com.anrisoftware.sscontrol.cmd.external.Cmd.SSH_CONNECTION_TIMEOUT;
import static com.anrisoftware.sscontrol.cmd.external.Cmd.SSH_CONTROL_MASTER;
import static com.anrisoftware.sscontrol.cmd.external.Cmd.SSH_CONTROL_PATH;
import static com.anrisoftware.sscontrol.cmd.external.Cmd.SSH_CONTROL_PERSIST_DURATION;
import static com.anrisoftware.sscontrol.cmd.external.Cmd.SSH_PORT;
import static com.anrisoftware.sscontrol.cmd.external.Cmd.SSH_USER;
import static java.lang.String.format;
import static org.apache.commons.io.FilenameUtils.getBaseName;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.anrisoftware.globalpom.durationformat.DurationFormatFactory;
import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.exec.external.scriptprocess.ScriptExecFactory;
import com.anrisoftware.globalpom.exec.internal.runcommands.RunCommands;
import com.anrisoftware.globalpom.exec.internal.runcommands.RunCommandsArg;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.resources.templates.external.TemplateResource;
import com.anrisoftware.sscontrol.cmd.external.core.ControlPathCreateDirErrorException;
import com.anrisoftware.sscontrol.cmd.external.core.ParsePropertiesErrorException;
import com.anrisoftware.sscontrol.cmd.internal.core.SshOptions.SshOptionsFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class CmdRun {

    public interface CmdRunFactory {

        CmdRun create(@Assisted Map<String, Object> args,
                @Assisted Object parent, @Assisted Threads threads,
                @Assisted String command);

    }

    private final Map<String, Object> args;

    private final Object parent;

    private final Threads threads;

    @Inject
    private CmdLogger log;

    @Inject
    private ScriptExecFactory scriptEx;

    @Inject
    private TemplatesProvider templates;

    @Inject
    private RunCommandsArg runCommandsArg;

    @Inject
    private PropertiesProvider propertiesProvider;

    @Inject
    private DurationFormatFactory durationFormatFactory;

    @Inject
    private SshOptionsFactory sshOptionsFactory;

    @Inject
    CmdRun(@Assisted Map<String, Object> args, @Assisted Object parent,
            @Assisted Threads threads, @Assisted String command) {
        this.args = new HashMap<String, Object>(args);
        this.args.put(COMMAND_KEY, command);
        this.parent = parent;
        this.threads = threads;
    }

    public ProcessTask call() throws CommandExecException {
        Map<String, Object> args = setupDefaults();
        setupSshDefaultArgs(args);
        String sshshell = getShellName(args);
        String template = format(COMMAND_NAME_FORMAT, "ssh_wrap_", sshshell);
        createSocketDir(args);
        RunCommands runCommands = runCommandsArg.runCommands(args, parent);
        TemplateResource res = templates.get().getResource(template);
        return runCommand(runCommands, res, args);
    }

    private void createSocketDir(Map<String, Object> args) {
        if (args.containsKey(SSH_CONTROL_PATH)) {
            String path = args.get(SSH_CONTROL_PATH).toString();
            File dir = new File(path).getParentFile();
            if (dir == null) {
                return;
            }
            if (!dir.isDirectory()) {
                boolean created = dir.mkdirs();
                if (!created) {
                    throw new ControlPathCreateDirErrorException(dir);
                }
            }
            dir.setWritable(true, true);
        }
    }

    private String getShellName(Map<String, Object> args) {
        String shell = args.get(SHELL).toString();
        shell = StringUtils.split(shell)[0];
        return getBaseName(shell);
    }

    private ProcessTask runCommand(RunCommands runCommands,
            TemplateResource res, Map<String, Object> args)
            throws CommandExecException {
        ProcessTask task;
        task = scriptEx.create(args, parent, threads, res, "sshCmd").call();
        runCommands.add(args.get(COMMAND_KEY), args);
        log.commandFinished(parent, task, args);
        return task;
    }

    private Map<String, Object> setupDefaults() {
        Map<String, Object> args = new HashMap<String, Object>(this.args);
        setupDefaults0(args);
        return args;
    }

    private void setupDefaults0(Map<String, Object> args) {
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

    private void setupSshDefaultArgs(Map<String, Object> args) {
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

    private static final String COMMAND_NAME_FORMAT = "%s%s";

    private static final String COMMAND_KEY = "command";

}
