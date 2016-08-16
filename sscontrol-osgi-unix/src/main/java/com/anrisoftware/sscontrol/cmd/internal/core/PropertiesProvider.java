package com.anrisoftware.sscontrol.cmd.internal.core;

import java.net.URL;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Provides the default command properties.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class PropertiesProvider extends AbstractContextPropertiesProvider {

    private static final URL res = PropertiesProvider.class
            .getResource("/default_cmd.properties");

    PropertiesProvider() {
        super(PropertiesProvider.class, res);
    }

}
