package com.anrisoftware.sscontrol.types.external;

import java.util.List;
import java.util.Set;

/**
 * Contains the host targets.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Targets {

    List<SshHost> getHosts(String name);

    Set<String> getGroups();

    void addTarget(Ssh ssh);

    void removeTarget(String name);

}
