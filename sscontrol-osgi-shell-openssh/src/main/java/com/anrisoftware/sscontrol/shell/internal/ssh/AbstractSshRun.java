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

import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_KEY;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.resources.templates.external.TemplateResource;
import com.anrisoftware.sscontrol.shell.external.SetupSshKeyException;
import com.anrisoftware.sscontrol.shell.internal.ssh.CmdArgs.CmdArgsFactory;
import com.anrisoftware.sscontrol.shell.internal.ssh.SshMaster.SshMasterFactory;

/**
 * Setups the SSH key and executes the command.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public abstract class AbstractSshRun extends AbstractCmdRun {

    @Inject
    private CmdArgsFactory argsFactory;

    @Inject
    private TemplatesProvider templates;

    @Inject
    private SshMasterFactory sshMasterFactory;

    protected AbstractSshRun(Map<String, Object> args, Object parent,
            Threads threads) {
        super(args, parent, threads);
    }

    @Override
    public ProcessTask call() throws CommandExecException {
        ArgsMap args = argsFactory.create(this.args).getArgs();
        setupSshKey(args);
        setupSshMaster(args);
        String template = getCmdTemplate(args);
        try {
            TemplateResource res = templates.get().getResource(template);
            return runCommand(res, args);
        } finally {
            cleanupCmd(args);
        }
    }

    /**
     * Returns the command template that is executed by SSH.
     */
    protected abstract String getCmdTemplate(ArgsMap args);

    private void setupSshKey(ArgsMap args) throws SetupSshKeyException {
        URI key = (URI) args.get(SSH_KEY);
        if (key == null) {
            return;
        }
        try {
            File tmp = File.createTempFile("robobee", null);
            IOUtils.copy(key.toURL().openStream(), new FileOutputStream(tmp));
            tmp.setReadable(false, false);
            tmp.setReadable(true, true);
            args.put(SSH_KEY, tmp);
        } catch (IOException e) {
            throw new SetupSshKeyException(e, key);
        }
    }

    private void setupSshMaster(ArgsMap args) throws CommandExecException {
        if (args.useSshMaster()) {
            sshMasterFactory.create(args, parent, threads).call();
        }
    }

    private void cleanupCmd(ArgsMap args) {
        File sshKey = (File) args.get(SSH_KEY);
        if (sshKey != null) {
            sshKey.delete();
        }
    }

}
