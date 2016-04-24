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
package com.anrisoftware.sscontrol.scripts.internal;

import static java.util.Collections.synchronizedMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.anrisoftware.sscontrol.scripts.external.ScriptsRepository;
import com.anrisoftware.sscontrol.types.external.SscontrolScript;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Scripts repository.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ScriptsRepositoryImpl implements ScriptsRepository {

    public interface ScriptsRepositoryImplFactory {

        ScriptsRepositoryImpl create();

        ScriptsRepositoryImpl create(@Assisted ScriptsRepository repository);

    }

    private final Map<String, SscontrolScript> scripts;

    @AssistedInject
    ScriptsRepositoryImpl() {
        this.scripts = synchronizedMap(new HashMap<String, SscontrolScript>());
    }

    @AssistedInject
    ScriptsRepositoryImpl(@Assisted ScriptsRepository repository) {
        this.scripts = synchronizedMap(new HashMap<String, SscontrolScript>());
        copyScripts(this.scripts, repository);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends SscontrolScript> T getScript(String name) {
        return (T) scripts.get(name);
    }

    @Override
    public Set<String> getScriptNames() {
        return scripts.keySet();
    }

    @Override
    public void putScript(String name, SscontrolScript script) {
        scripts.put(name, script);
    }

    @Override
    public void removeScript(String name) {
        scripts.remove(name);
    }

    private void copyScripts(Map<String, SscontrolScript> scripts,
            ScriptsRepository repository) {
        for (String name : repository.getScriptNames()) {
            scripts.put(name, repository.getScript(name));
        }
    }

}
