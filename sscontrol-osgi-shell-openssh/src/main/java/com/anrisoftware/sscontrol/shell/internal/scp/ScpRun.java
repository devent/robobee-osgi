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

import static com.anrisoftware.sscontrol.shell.external.Cmd.PRIVILEGED_ARG;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.resources.templates.external.TemplateResource;
import com.anrisoftware.sscontrol.shell.external.ssh.AbstractSshRun;
import com.anrisoftware.sscontrol.shell.internal.scp.CopyPrivilegedFileWorker.CopyPrivilegedFileWorkerFactory;
import com.anrisoftware.sscontrol.shell.internal.scp.CopyUnprivilegedFileWorker.CopyUnprivilegedFileWorkerFactory;
import com.anrisoftware.sscontrol.shell.internal.scp.PushPrivilegedFileWorker.PushPrivilegedFileWorkerFactory;
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
    private CopyPrivilegedFileWorkerFactory copyPrivileged;

    @Inject
    private PushPrivilegedFileWorkerFactory pushPrivileged;

    @Inject
    private CopyUnprivilegedFileWorkerFactory copyUnprivileged;

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
        if (remoteSrc) {
            return copyPrivileged.create(args, parent, threads, templates.get(),
                    res, getRemoteTempDir(), getCopyFileCommands()).call();
        }
        if (remoteDest) {
            return pushPrivileged.create(args, parent, threads, templates.get(),
                    res, getRemoteTempDir(), getPushFileCommands()).call();
        }
        return task;
    }

    private ProcessTask runUnprivileged(TemplateResource res,
            Map<String, Object> args) throws CommandExecException {
        return copyUnprivileged
                .create(args, parent, threads, templates.get(), res).call();
    }

}
