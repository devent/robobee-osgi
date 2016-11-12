package com.anrisoftware.sscontrol.shell.external;

import java.util.Map;

import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * Scp command.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Scp {

    /**
     * Factory to create the scp command.
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ScpFactory {

        Scp create(@Assisted Map<String, Object> args, @Assisted SshHost host,
                @Assisted("parent") Object parent, @Assisted Threads threads,
                @Assisted("log") Object log);
    }

    /**
     * Executes the scp command.
     * 
     * @throws AppException
     */
    ProcessTask call() throws AppException;

}
