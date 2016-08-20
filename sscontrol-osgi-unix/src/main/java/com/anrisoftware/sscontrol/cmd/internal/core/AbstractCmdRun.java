package com.anrisoftware.sscontrol.cmd.internal.core;

import static com.anrisoftware.sscontrol.cmd.external.Cmd.SHELL;
import static com.anrisoftware.sscontrol.cmd.external.Cmd.SSH_CONTROL_PATH;
import static org.apache.commons.io.FilenameUtils.getBaseName;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.exec.external.scriptprocess.ScriptExecFactory;
import com.anrisoftware.globalpom.exec.internal.runcommands.RunCommands;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.resources.templates.external.TemplateResource;
import com.anrisoftware.sscontrol.cmd.external.core.ControlPathCreateDirErrorException;

public abstract class AbstractCmdRun {

    protected final Map<String, Object> args;

    protected final Object parent;

    protected final Threads threads;

    @Inject
    private CmdLogger log;

    @Inject
    private ScriptExecFactory scriptEx;

    protected AbstractCmdRun(Map<String, Object> args, Object parent,
            Threads threads) {
        this.args = new HashMap<String, Object>(args);
        this.parent = parent;
        this.threads = threads;
    }

    public abstract ProcessTask call() throws CommandExecException;

    protected void createSocketDir(Map<String, Object> args) {
        if (!args.containsKey(SSH_CONTROL_PATH)) {
            return;
        }
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

    protected String getShellName(Map<String, Object> args) {
        String shell = args.get(SHELL).toString();
        shell = StringUtils.split(shell)[0];
        return getBaseName(shell);
    }

    protected ProcessTask runCommand(RunCommands runCommands,
            TemplateResource res, Map<String, Object> args)
            throws CommandExecException {
        ProcessTask task;
        task = scriptEx.create(args, parent, threads, res, "sshCmd").call();
        runCommands.add(args.get(COMMAND_KEY), args);
        log.commandFinished(parent, task, args);
        return task;
    }

    protected static final String COMMAND_KEY = "command";

}
