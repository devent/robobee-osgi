package com.anrisoftware.sscontrol.unix.internal.core;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.cmd.external.Cmd;
import com.anrisoftware.sscontrol.types.external.SscontrolServiceScript;
import com.anrisoftware.sscontrol.unix.internal.core.CmdRun.CmdRunFactory;

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
    public ProcessTask call(Map<String, Object> args, String command,
            SscontrolServiceScript parent) throws CommandExecException {
        return cmdRunFactory.create(command, parent,
                (Threads) parent.getThreads(), parent.getDefaultProperties(),
                parent.getProfile(), parent.getLog(), args).call();
    }

}
