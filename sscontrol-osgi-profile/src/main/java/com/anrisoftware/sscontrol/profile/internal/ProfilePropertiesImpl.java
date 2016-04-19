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
import static java.util.Collections.synchronizedMap;
import groovy.lang.GroovyObjectSupport;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.profile.external.ProfileProperties;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.ArgumentInvalidException;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Implements service profile.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ProfilePropertiesImpl extends GroovyObjectSupport implements
        ProfileProperties {

    public interface ProfilePropertiesImplFactory {

        ProfilePropertiesImpl create();

        ProfilePropertiesImpl create(@Assisted ProfileProperties properties);

    }

    private final Map<String, Object> properties;

    @Inject
    private ProfilePropertiesImplLogger log;

    private String name;

    @AssistedInject
    ProfilePropertiesImpl() {
        this.properties = synchronizedMap(new HashMap<String, Object>());
    }

    @AssistedInject
    ProfilePropertiesImpl(@Assisted ProfileProperties properties) {
        this.name = properties.getName();
        HashMap<String, Object> dest = new HashMap<String, Object>();
        this.properties = synchronizedMap(dest);
        copyProperties(dest, properties);
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

    @SuppressWarnings("unchecked")
    @Override
    public Object getProperty(String name) {
        return properties.get(name);
    }

    public void putProperty(String name, Object value)
            throws ArgumentInvalidException {
        checkBlankArg(name, "name");
        properties.put(name, value);
        log.propertyAdded(this, name, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getProperty(String name, T defaultValue) {
        Object value = getProperty(name);
        return (T) (value != null ? value : defaultValue);
    }

    @Override
    public Set<String> getPropertyNames() {
        return properties.keySet();
    }

    private void copyProperties(Map<String, Object> dest,
            ProfileProperties properties) {
        for (String key : properties.getPropertyNames()) {
            String property = properties.getProperty(key);
            dest.put(key, property);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name)
                .append("properties", properties.size()).toString();
    }
}
