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
