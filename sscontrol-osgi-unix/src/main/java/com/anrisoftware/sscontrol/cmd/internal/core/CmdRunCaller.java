package com.anrisoftware.sscontrol.cmd.internal.core;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.cmd.external.Cmd;
import com.anrisoftware.sscontrol.cmd.internal.core.CmdRun.CmdRunFactory;

/**
 * Wrapper around command call method.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class CmdRunCaller implements Cmd {

    @Inject
    private CmdRunFactory cmdRunFactory;

    @Override
    public ProcessTask call(Map<String, Object> args, Object parent,
            Threads threads, String command) throws CommandExecException {
        return cmdRunFactory.create(args, parent, threads, command).call();
    }

}
