package com.anrisoftware.sscontrol.cmd.internal.core;

import static java.lang.String.format;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.resources.templates.external.TemplateResource;
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
        if (args.useSshMaster()) {
            sshMasterFactory.create(args, parent, threads).call();
        }
        String sshshell = getShellName(args);
        String template = format(COMMAND_NAME_FORMAT, "ssh_wrap_", sshshell);
        TemplateResource res = templates.get().getResource(template);
        return runCommand(res, args);
    }

    private static final String COMMAND_NAME_FORMAT = "%s%s";

}
