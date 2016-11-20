package com.anrisoftware.sscontrol.shell.external.ssh;

import java.util.Map;

import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface SshArgs extends Map<String, Object> {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface SshArgsFactory {

        SshArgs create(@Assisted Map<String, Object> args);

    }

    boolean useSshMaster();

}
