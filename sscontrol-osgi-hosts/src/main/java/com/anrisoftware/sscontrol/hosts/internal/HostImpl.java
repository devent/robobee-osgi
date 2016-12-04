package com.anrisoftware.sscontrol.hosts.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        Host create(Map<String, Object> args);

    }

    private final String address;

    private final List<String> aliases;

    private final String host;

    private final String identifier;

    @SuppressWarnings("unchecked")
    @Inject
    HostImpl(@Assisted Map<String, Object> args) {
        this.address = args.get("address").toString();
        this.host = args.get("host").toString();
        this.aliases = new ArrayList<String>(
                (List<String>) args.get("aliases"));
        this.identifier = args.get("identifier").toString();
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    private static final String ALIASES = "aliases";

    private static final String HOST = "host";

    private static final String IDENTIFIER = "identifier";

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(address).append(HOST, host)
                .append(ALIASES, aliases).append(IDENTIFIER, identifier)
                .toString();
    }

}
