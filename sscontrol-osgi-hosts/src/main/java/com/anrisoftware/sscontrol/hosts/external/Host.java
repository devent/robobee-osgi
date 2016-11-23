package com.anrisoftware.sscontrol.hosts.external;

import java.util.List;

/**
 * Host with address and aliases.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Host {

    String getAddress();

    String getHost();

    List<String> getAliases();

}
