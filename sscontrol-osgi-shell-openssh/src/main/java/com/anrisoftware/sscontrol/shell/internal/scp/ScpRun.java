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
package com.anrisoftware.sscontrol.shell.internal.scp;

import static com.anrisoftware.sscontrol.copy.external.Copy.DEST_ARG;
import static com.anrisoftware.sscontrol.fetch.external.Fetch.SRC_ARG;
import static com.anrisoftware.sscontrol.shell.external.Cmd.PRIVILEGED_ARG;
import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.resources.templates.external.TemplateResource;
import com.anrisoftware.sscontrol.shell.internal.ssh.AbstractSshRun;
import com.google.inject.assistedinject.Assisted;

/**
 * Executes the scp command.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ScpRun extends AbstractSshRun {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ScpRunFactory {

        ScpRun create(@Assisted Map<String, Object> args,
                @Assisted Object parent, @Assisted Threads threads);

    }

    @Inject
    private ScpRunLogger log;

    @Inject
    ScpRun(@Assisted Map<String, Object> args, @Assisted Object parent,
            @Assisted Threads threads) {
        super(args, parent, threads);
    }

    @Override
    protected String getCmdTemplate() {
        return "scp";
    }

    @Override
    protected ProcessTask runCommand(TemplateResource res,
            Map<String, Object> args) throws CommandExecException {
        ProcessTask task = null;
        Boolean privileged = (Boolean) args.get(PRIVILEGED_ARG);
        if (privileged == null || !privileged) {
            task = runUnprivileged(res, args);
        } else if (privileged != null || privileged) {
            task = runPrivileged(res, args);
        }
        return task;
    }

    private ProcessTask runPrivileged(TemplateResource res,
            Map<String, Object> args) throws CommandExecException {
        ProcessTask task = null;
        boolean remoteSrc = (Boolean) args.get("remoteSrc");
        boolean remoteDest = (Boolean) args.get("remoteDest");
        String tmp = getRemoteTempDir();
        String src = args.get(SRC_ARG).toString();
        String dest = args.get(DEST_ARG).toString();
        if (remoteSrc) {
            Map<String, Object> a = new HashMap<String, Object>(args);
            a.put(PRIVILEGED_ARG, true);
            a.put(COMMAND_ARG, format(getCopyFileCommands(), tmp, src));
            task = runCmd(a);
            args.put(SRC_ARG, format("%s/%s", tmp, src));
            task = runScript(res, args);
        }
        if (remoteDest) {
            args.put(DEST_ARG, format("%s/%s", tmp, src));
            task = runScript(res, args);
            Map<String, Object> a = new HashMap<String, Object>(args);
            a.put(PRIVILEGED_ARG, true);
            a.put(COMMAND_ARG, format(getPushFileCommands(), tmp, src, dest));
            task = runCmd(a);
        }
        return task;
    }

    private ProcessTask runUnprivileged(TemplateResource res,
            Map<String, Object> args) throws CommandExecException {
        return runScript(res, args);
    }

    private ProcessTask runScript(TemplateResource res,
            Map<String, Object> args) throws CommandExecException {
        ProcessTask task;
        task = scriptEx.create(args, parent, threads, res, "scpCmd").call();
        log.commandFinished(parent, task, args);
        return task;
    }

    private ProcessTask runCmd(Map<String, Object> args)
            throws CommandExecException {
        TemplateResource res = templates.get().getResource("ssh_wrap_bash");
        ProcessTask task;
        task = scriptEx.create(args, parent, threads, res, "sshCmd").call();
        log.commandFinished(parent, task, args);
        return task;
    }

}
