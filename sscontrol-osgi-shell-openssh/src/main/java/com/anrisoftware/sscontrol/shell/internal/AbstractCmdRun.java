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
package com.anrisoftware.sscontrol.shell.internal;

import static com.anrisoftware.sscontrol.shell.external.Cmd.SHELL;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_CONTROL_PATH;
import static org.apache.commons.io.FilenameUtils.getBaseName;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.exec.external.scriptprocess.ScriptExecFactory;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.resources.templates.external.TemplateResource;
import com.anrisoftware.sscontrol.shell.external.ControlPathCreateDirErrorException;

/**
 * Setups the SSH master socket.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public abstract class AbstractCmdRun {

    protected final Map<String, Object> args;

    protected final Object parent;

    protected final Threads threads;

    @Inject
    protected ScriptExecFactory scriptEx;

    @Inject
    protected CmdLogger log;

    protected AbstractCmdRun(Map<String, Object> args, Object parent,
            Threads threads) {
        this.args = new HashMap<String, Object>(args);
        this.parent = parent;
        this.threads = threads;
    }

    /**
     * Executes the command.
     */
    public abstract ProcessTask call() throws CommandExecException;

    /**
     * Creates the master socket directory.
     */
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

    protected ProcessTask runCommand(TemplateResource res,
            Map<String, Object> args) throws CommandExecException {
        ProcessTask task;
        task = scriptEx.create(args, parent, threads, res, "sshCmd").call();
        log.commandFinished(parent, task, args);
        return task;
    }

    protected static final String COMMAND_KEY = "command";

}
