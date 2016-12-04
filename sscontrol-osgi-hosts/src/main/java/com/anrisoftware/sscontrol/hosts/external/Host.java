package com.anrisoftware.sscontrol.hosts.external;

import java.util.List;

/**
 * Host with address and aliases.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Host {

    /**
     * Returns the host IP address.
     */
    String getAddress();

    /**
     * Returns the name of the host.
     */
    String getHost();

    /**
     * Returns the aliases of the host.
     */
    List<String> getAliases();

    /**
     * Returns on what the host should be identified.
     */
    String getIdentifier();

}
