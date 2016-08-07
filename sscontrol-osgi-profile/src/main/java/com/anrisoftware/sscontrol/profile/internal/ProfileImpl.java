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
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.ArgumentInvalidException;
import com.anrisoftware.sscontrol.types.external.Profile;
import com.anrisoftware.sscontrol.types.external.ProfileProperties;
import com.anrisoftware.sscontrol.types.external.ProfilePropertiesService;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import groovy.lang.GroovyObjectSupport;

/**
 * Implements profile.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ProfileImpl extends GroovyObjectSupport implements Profile {

    public interface ProfileImplFactory {

        ProfileImpl create();

        ProfileImpl create(@Assisted Profile profile);

    }

    private final Map<String, ProfileProperties> entries;

    @Inject
    private ProfileImplLogger log;

    @Inject
    private ProfilePropertiesService propertiesService;

    private String name;

    @AssistedInject
    ProfileImpl() {
        this.entries = synchronizedMap(
                new LinkedHashMap<String, ProfileProperties>());
    }

    @AssistedInject
    ProfileImpl(@Assisted Profile profile,
            ProfilePropertiesService propertiesService) {
        this.name = profile.getName();
        HashMap<String, ProfileProperties> dest = new LinkedHashMap<String, ProfileProperties>();
        this.entries = synchronizedMap(dest);
        copyProperties(dest, profile, propertiesService);
    }

    public Profile call(String name) throws AppException {
        setName(name);
        return this;
    }

    public Object methodMissing(String name, Object args) throws AppException {
        ProfileProperties p = propertiesService.create(name);
        addEntry(name, p);
        return p;
    }

    public Object propertyMissing(String name, Object args)
            throws AppException {
        ProfileProperties p = propertiesService.create(name);
        addEntry(name, p);
        return p;
    }

    public void addEntry(String name, ProfileProperties properties) {
        entries.put(name, properties);
        log.profileEntryAdded(this, properties);
    }

    @Override
    public ProfileProperties getEntry(String name) {
        return entries.get(name);
    }

    @Override
    public List<String> getEntryNames() {
        return unmodifiableList(new ArrayList<String>(entries.keySet()));
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
    public String toString() {
        return new ToStringBuilder(this).append("name", name)
                .append("entries", entries.size()).toString();
    }

    private void copyProperties(HashMap<String, ProfileProperties> dest,
            Profile profile, ProfilePropertiesService propertiesService) {
        for (String name : profile.getEntryNames()) {
            ProfileProperties p = propertiesService
                    .create(profile.getEntry(name));
            dest.put(name, p);
        }
    }

}
