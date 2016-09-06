package com.anrisoftware.sscontrol.types.external;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class HostServicePropertiesUtil {

    /**
     * Provides the property statement for the host service.
     * 
     * <pre>
     * property << 'name = value'
     * </pre>
     */
    @SuppressWarnings("serial")
    public static List<String> propertyStatement(
            final HostServiceProperties serviceProperties) {
        return new ArrayList<String>() {

            @Override
            public boolean add(String e) {
                serviceProperties.addProperty(e);
                return true;
            }

            @Override
            public void add(int index, String element) {
                serviceProperties.addProperty(element);
            }

            @Override
            public boolean addAll(Collection<? extends String> c) {
                for (String string : c) {
                    add(string);
                }
                return true;
            }

            @Override
            public boolean addAll(int index, Collection<? extends String> c) {
                for (String string : c) {
                    add(string);
                }
                return true;
            }
        };
    }

}
