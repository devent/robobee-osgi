/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-unix.
 *
 * sscontrol-osgi-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-unix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.cmd.internal.core;

import static com.anrisoftware.sscontrol.cmd.external.Cmd.SSH_KEY;
import static java.lang.String.format;

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
import com.anrisoftware.sscontrol.cmd.external.core.SetupSshKeyException;
import com.anrisoftware.sscontrol.cmd.internal.core.CmdArgs.CmdArgsFactory;
import com.anrisoftware.sscontrol.cmd.internal.core.SshMaster.SshMasterFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class CmdRun extends AbstractCmdRun {

    public interface CmdRunFactory {

        CmdRun create(@Assisted Map<String, Object> args,
                @Assisted Object parent, @Assisted Threads threads,
                @Assisted String command);

    }

    @Inject
    private CmdArgsFactory argsFactory;

    @Inject
    private TemplatesProvider templates;

    @Inject
    private SshMasterFactory sshMasterFactory;

    @Inject
    CmdRun(@Assisted Map<String, Object> args, @Assisted Object parent,
            @Assisted Threads threads, @Assisted String command) {
        super(args, parent, threads);
        this.args.put(COMMAND_KEY, command);
    }

    @Override
    public ProcessTask call() throws CommandExecException {
        ArgsMap args = argsFactory.create(this.args).getArgs();
        setupSshKey(args);
        setupSshMaster(args);
        String sshshell = getShellName(args);
        String template = format(COMMAND_NAME_FORMAT, "ssh_wrap_", sshshell);
        try {
            TemplateResource res = templates.get().getResource(template);
            return runCommand(res, args);
        } finally {
            cleanupCmd(args);
        }
    }

    private void setupSshMaster(ArgsMap args) throws CommandExecException {
        if (args.useSshMaster()) {
            sshMasterFactory.create(args, parent, threads).call();
        }
    }

    private void setupSshKey(ArgsMap args) throws SetupSshKeyException {
        if (args.containsKey(SSH_KEY)) {
            setupSshKey0(args);
        }
    }

    private void setupSshKey0(ArgsMap args) throws SetupSshKeyException {
        URI key = (URI) args.get(SSH_KEY);
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

    private void cleanupCmd(ArgsMap args) {
        if (args.containsKey(SSH_KEY)) {
            File sshKey = (File) args.get(SSH_KEY);
            sshKey.delete();
        }
    }

    private static final String COMMAND_NAME_FORMAT = "%s%s";

}
