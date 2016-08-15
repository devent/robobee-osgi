package com.anrisoftware.sscontrol.unix.internal.core;

import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.exec.external.scriptprocess.ScriptExecFactory;
import com.anrisoftware.globalpom.exec.internal.runcommands.RunCommands;
import com.anrisoftware.globalpom.exec.internal.runcommands.RunCommandsArg;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.resources.templates.external.TemplateResource;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class CmdRun {

    public interface CmdRunFactory {

        CmdRun create(@Assisted Object parent, @Assisted Threads threads,
                @Assisted String command, @Assisted Map<String, Object> args);

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
    CmdRun(@Assisted Object parent, @Assisted Threads threads,
            @Assisted String command, @Assisted Map<String, Object> args) {
        this.args = new HashMap<String, Object>(args);
        this.parent = parent;
        this.threads = threads;
        args.put("command", command);
    }

    public ProcessTask call() throws CommandExecException {
        String sshbash = getSshBash(args);
        String commandName;
        commandName = format(COMMAND_NAME_FORMAT, "ssh_wrap_", sshbash);
        RunCommands runCommands = runCommandsArg.runCommands(args, parent);
        TemplateResource res = templates.get().getResource("commandName");
        return runCommand(commandName, runCommands, res);
    }

    private ProcessTask runCommand(String commandName, RunCommands runCommands,
            TemplateResource res) throws CommandExecException {
        ProcessTask task;
        task = scriptEx.create(args, parent, threads, res, commandName).call();
        runCommands.add(args.get(COMMAND_KEY), args);
        log.commandFinished(parent, task, args);
        return task;
    }

    private String getSshBash(Map<String, Object> args) {
        Object arg = args.get("sshShell");
        if (arg == null) {
            return "bash";
        }
        return args.get("sshShell").toString();
    }

    private static final String COMMAND_NAME_FORMAT = "%s%s";

    private static final String COMMAND_KEY = "command";

}
