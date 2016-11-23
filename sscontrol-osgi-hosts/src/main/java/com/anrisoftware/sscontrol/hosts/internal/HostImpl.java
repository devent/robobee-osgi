package com.anrisoftware.sscontrol.hosts.internal;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.hosts.external.Host;
import com.google.inject.assistedinject.Assisted;

/**
 * Host with address and aliases.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class HostImpl implements Host {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface HostImplFactory {

        Host create(@Assisted("address") String address,
                @Assisted("host") String host, @Assisted List<String> aliases);

    }

    private final String address;

    private final List<String> aliases;

    private final String host;

    @Inject
    HostImpl(@Assisted("address") String address, @Assisted("host") String host,
            @Assisted List<String> aliases) {
        this.address = address;
        this.host = host;
        this.aliases = new ArrayList<String>(aliases);
    }

    /**
     * Returns the host IP address.
     */
    @Override
    public String getAddress() {
        return address;
    }

    /**
     * Returns the name of the host.
     */
    @Override
    public String getHost() {
        return host;
    }

    /**
     * Returns the aliases of the host.
     */
    @Override
    public List<String> getAliases() {
        return aliases;
    }

    private static final String ALIASES = "aliases";

    private static final String HOST = "host";

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(address).append(HOST, host)
                .append(ALIASES, aliases).toString();
    }

}
