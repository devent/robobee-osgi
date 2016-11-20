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

import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_KEY_ARG;
import static java.lang.String.format;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.resources.templates.external.TemplateResource;
import com.anrisoftware.sscontrol.shell.external.SetupSshKeyException;
import com.anrisoftware.sscontrol.shell.internal.ssh.SshMaster.SshMasterFactory;

/**
 * Setups the SSH key and executes the command.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public abstract class AbstractSshRun extends AbstractCmdRun {

    @Inject
    protected TemplatesProvider templates;

    @Inject
    private AbstractSshRunLogger log;

    @Inject
    private SshMasterFactory sshMasterFactory;

    @Inject
    protected LinuxPropertiesProvider linuxPropertiesProvider;

    protected AbstractSshRun(Map<String, Object> args, Object parent,
            Threads threads) {
        super(args, parent, threads);
    }

    @Override
    public ProcessTask call() throws CommandExecException {
        setupSshKey();
        setupSshMaster();
        setupRemote();
        try {
            String template = getCmdTemplate();
            TemplateResource res = templates.get().getResource(template);
            return runCommand(res, args);
        } finally {
            cleanupCmd();
        }
    }

    /**
     * Returns the command template that is executed by SSH.
     */
    protected abstract String getCmdTemplate();

    protected String getRemoteTempDir() {
        return linuxPropertiesProvider.getRemoteTempDir();
    }

    protected String getCopyFileCommands() {
        return linuxPropertiesProvider.getCopyFileCommands();
    }

    protected String getPushFileCommands() {
        return linuxPropertiesProvider.getPushFileCommands();
    }

    private void setupRemote() throws CommandExecException {
        ProcessTask task;
        String template = "ssh_wrap_bash";
        TemplateResource res = templates.get().getResource(template);
        Map<String, Object> args = new HashMap<String, Object>();
        args.putAll(this.args);
        args.put("privileged", true);
        args.put(COMMAND_ARG, format(linuxPropertiesProvider.getSetupCommands(),
                linuxPropertiesProvider.getRemoteTempDir()));
        task = scriptEx.create(args, parent, threads, res, "sshCmd").call();
        log.commandFinished(parent, task, args);
    }

    private void setupSshKey() throws SetupSshKeyException {
        URI key = (URI) args.get(SSH_KEY_ARG);
        if (key == null) {
            return;
        }
        try {
            File tmp = File.createTempFile("robobee", null);
            IOUtils.copy(key.toURL().openStream(), new FileOutputStream(tmp));
            tmp.setReadable(false, false);
            tmp.setReadable(true, true);
            args.put(SSH_KEY_ARG, tmp);
        } catch (IOException e) {
            throw new SetupSshKeyException(e, key);
        }
    }

    private void setupSshMaster() throws CommandExecException {
        if (args.useSshMaster()) {
            sshMasterFactory.create(argsMap, parent, threads).call();
        }
    }

    private void cleanupCmd() {
        File sshKey = (File) args.get(SSH_KEY_ARG);
        if (sshKey != null) {
            sshKey.delete();
        }
    }

}
