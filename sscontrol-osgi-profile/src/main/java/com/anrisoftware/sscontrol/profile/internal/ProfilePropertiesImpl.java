/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.profile.internal;

import static com.anrisoftware.sscontrol.types.external.ArgumentInvalidException.checkBlankArg;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.Format;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.propertiesutils.StringToType;
import com.anrisoftware.propertiesutils.TypedAllProperties;
import com.anrisoftware.propertiesutils.TypedAllPropertiesFactory;
import com.anrisoftware.propertiesutils.TypedAllPropertiesService;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.ArgumentInvalidException;
import com.anrisoftware.sscontrol.types.external.ProfileProperties;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import groovy.lang.GroovyObjectSupport;

/**
 * Implements service profile.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ProfilePropertiesImpl extends GroovyObjectSupport
        implements ProfileProperties {

    public interface ProfilePropertiesImplFactory {

        ProfilePropertiesImpl create();

        ProfilePropertiesImpl create(@Assisted ProfileProperties properties);

    }

    private final Properties properties;

    private final TypedAllPropertiesFactory typedAllPropertiesFactory;

    private final TypedAllProperties typedProperties;

    @Inject
    private ProfilePropertiesImplLogger log;

    private String name;

    @AssistedInject
    ProfilePropertiesImpl(TypedAllPropertiesFactory propertiesFactory) {
        this.properties = new Properties();
        this.typedAllPropertiesFactory = propertiesFactory;
        this.typedProperties = propertiesFactory.create(properties);
    }

    @AssistedInject
    ProfilePropertiesImpl(@Assisted ProfileProperties profileProperties,
            TypedAllPropertiesService typedPropertiesService) {
        this.name = profileProperties.getName();
        this.properties = new Properties();
        this.typedAllPropertiesFactory = typedPropertiesService;
        this.typedProperties = typedPropertiesService.create(properties);
        copyProperties(this.properties, properties);
    }

    public void propertyMissing(String name, Object value) throws AppException {
        putProperty(name, value);
    }

    public void setName(String name) throws ArgumentInvalidException {
        checkBlankArg(name, "name");
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getProperty(String name) {
        return properties.getProperty(name);
    }

    public String getProperty(String name, ContextProperties defaults) {
        String value = properties.getProperty(name);
        if (value != null) {
            return value;
        } else {
            return defaults.getProperty(name);
        }
    }

    @Override
    public String getProperty(String name, String defaultValue) {
        return properties.getProperty(name, defaultValue);
    }

    public void putProperty(String name, Object value)
            throws ArgumentInvalidException {
        checkBlankArg(name, "name");
        properties.put(name, value);
        log.propertyAdded(this, name, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<String> getPropertyNames() {
        @SuppressWarnings("rawtypes")
        Set set = new HashSet(properties.keySet());
        return set;
    }

    public Period getPeriodProperty(String key, ContextProperties defaults) {
        Period value = typedProperties.getPeriodProperty(key);
        if (value != null) {
            return value;
        } else {
            return typedAllPropertiesFactory.create(defaults)
                    .getPeriodProperty(key);
        }
    }

    public Period getPeriodProperty(String key, PeriodFormatter formatter,
            ContextProperties defaults) {
        Period value = typedProperties.getPeriodProperty(key, formatter);
        if (value != null) {
            return value;
        } else {
            return typedAllPropertiesFactory.create(defaults)
                    .getPeriodProperty(key);
        }
    }

    public Duration getDurationProperty(String key,
            ContextProperties defaults) {
        Duration value = typedProperties.getDurationProperty(key);
        if (value != null) {
            return value;
        } else {
            return typedAllPropertiesFactory.create(defaults)
                    .getDurationProperty(key);
        }
    }

    public Duration getDurationProperty(String key, PeriodFormatter formatter,
            ContextProperties defaults) {
        Duration value = typedProperties.getDurationProperty(key, formatter);
        if (value != null) {
            return value;
        } else {
            return typedAllPropertiesFactory.create(defaults)
                    .getDurationProperty(key, formatter);
        }
    }

    public Boolean getBooleanProperty(String key, ContextProperties defaults) {
        Boolean value = typedProperties.getBooleanProperty(key);
        if (value != null) {
            return value;
        } else {
            return typedAllPropertiesFactory.create(defaults)
                    .getBooleanProperty(key);
        }
    }

    public Number getNumberProperty(String key, ContextProperties defaults) {
        Number value = typedProperties.getNumberProperty(key);
        if (value != null) {
            return value;
        } else {
            return typedAllPropertiesFactory.create(defaults)
                    .getNumberProperty(key);
        }
    }

    public Character getCharProperty(String key, ContextProperties defaults) {
        Character value = typedProperties.getCharProperty(key);
        if (value != null) {
            return value;
        } else {
            return typedAllPropertiesFactory.create(defaults)
                    .getCharProperty(key);
        }
    }

    public Charset getCharsetProperty(String key, ContextProperties defaults) {
        Charset value = typedProperties.getCharsetProperty(key);
        if (value != null) {
            return value;
        } else {
            return typedAllPropertiesFactory.create(defaults)
                    .getCharsetProperty(key);
        }
    }

    public URL getURLProperty(String key, ContextProperties defaults)
            throws MalformedURLException {
        URL value = typedProperties.getURLProperty(key);
        if (value != null) {
            return value;
        } else {
            return typedAllPropertiesFactory.create(defaults)
                    .getURLProperty(key);
        }
    }

    public URI getURIProperty(String key, ContextProperties defaults)
            throws URISyntaxException {
        URI value = typedProperties.getURIProperty(key);
        if (value != null) {
            return value;
        } else {
            return typedAllPropertiesFactory.create(defaults)
                    .getURIProperty(key);
        }
    }

    public File getFileProperty(String key, ContextProperties defaults) {
        File value = typedProperties.getFileProperty(key);
        if (value != null) {
            return value;
        } else {
            return typedAllPropertiesFactory.create(defaults)
                    .getFileProperty(key);
        }
    }

    /**
     * Returns the profile path property. If the profile property was not set
     * return the default value from the default properties. If the path is not
     * absolute then it is assume to be under the specified parent directory.
     *
     * @param key
     *            the key of the profile property.
     *
     * @param parent
     *            the parent {@link File} directory.
     *
     * @param p
     *            default {@link ContextProperties} properties.
     *
     * @return the profile file {@link File} path or {@code null}.
     */
    public File getFileProperty(String key, File parent,
            ContextProperties defaults) {
        Object value = getProperty(key, defaults);
        if (value == null) {
            return null;
        }
        File path;
        if (value instanceof File) {
            path = (File) value;
        } else {
            path = new File(value.toString());
        }
        return path.isAbsolute() ? path : new File(parent, path.getPath());
    }

    public <T> T getTypedProperty(String key, Format format,
            ContextProperties defaults) throws ParseException {
        T value = typedProperties.getTypedProperty(key, format);
        if (value != null) {
            return value;
        } else {
            return typedAllPropertiesFactory.create(defaults)
                    .getTypedProperty(key, format);
        }
    }

    public <T> List<T> getTypedListProperty(String key, Format format,
            ContextProperties defaults) throws ParseException {
        List<T> value = typedProperties.getTypedListProperty(key, format);
        if (value != null) {
            return value;
        } else {
            return typedAllPropertiesFactory.create(defaults)
                    .getTypedListProperty(key, format);
        }
    }

    public <T> List<T> getTypedListProperty(String key, Format format,
            String separatorChars, ContextProperties defaults)
                    throws ParseException {
        List<T> value = typedProperties.getTypedListProperty(key, format,
                separatorChars);
        if (value != null) {
            return value;
        } else {
            return typedAllPropertiesFactory.create(defaults)
                    .getTypedListProperty(key, format, separatorChars);
        }
    }

    public <T> List<T> getTypedListProperty(String key,
            StringToType<T> stringToType, ContextProperties defaults)
                    throws ParseException {
        List<T> value = typedProperties.getTypedListProperty(key, stringToType);
        if (value != null) {
            return value;
        } else {
            return typedAllPropertiesFactory.create(defaults)
                    .getTypedListProperty(key, stringToType);
        }
    }

    public <T> List<T> getTypedListProperty(String key,
            StringToType<T> stringToType, String separatorChars,
            ContextProperties defaults) throws ParseException {
        List<T> value = typedProperties.getTypedListProperty(key, stringToType,
                separatorChars);
        if (value != null) {
            return value;
        } else {
            return typedAllPropertiesFactory.create(defaults)
                    .getTypedListProperty(key, stringToType, separatorChars);
        }
    }

    public List<String> getListProperty(String key,
            ContextProperties defaults) {
        List<String> value = typedProperties.getListProperty(key);
        if (value != null) {
            return value;
        } else {
            return typedAllPropertiesFactory.create(defaults)
                    .getListProperty(key);
        }
    }

    public List<String> getListProperty(String key, String separatorChars,
            ContextProperties defaults) {
        List<String> value = typedProperties.getListProperty(key,
                separatorChars);
        if (value != null) {
            return value;
        } else {
            return typedAllPropertiesFactory.create(defaults)
                    .getListProperty(key, separatorChars);
        }
    }

    private void copyProperties(Properties dest, Properties properties) {
        for (Entry<Object, Object> key : properties.entrySet()) {
            String property = properties.getProperty(key.getKey().toString());
            dest.put(key, property);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name)
                .append("properties", properties.size()).toString();
    }
}
