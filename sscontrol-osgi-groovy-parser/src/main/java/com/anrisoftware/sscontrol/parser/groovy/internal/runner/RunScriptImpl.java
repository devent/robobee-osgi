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
package com.anrisoftware.sscontrol.parser.groovy.internal.runner;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.parser.external.RunScript;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.HostService;
import com.anrisoftware.sscontrol.types.external.HostServices;
import com.anrisoftware.sscontrol.types.external.PreHost;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import groovy.lang.GroovyShell;

/**
 * Executes the script.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class RunScriptImpl implements RunScript {

    public interface RunScriptImplFactory {

        RunScriptImpl create(@Assisted HostServices repository);

    }

    private final HostServices repository;

    @AssistedInject
    RunScriptImpl(@Assisted HostServices repository) {
        this.repository = repository;
    }

    @Override
    public HostService run(String scriptName, Map<String, Object> variables,
            PreHost prescript) throws AppException {
        /*
         * SscontrolScript script = repository.getScript(scriptName); Profile
         * profile = repository.getScript(profileName); GroovyShell shell =
         * createShell(variables, profile, prescript); Reader reader =
         * openScript(url); Script service = evaluateScript(reader, shell, url);
         * service.setThreads(threads); registry.addService(service);
         */
        return null;
    }

    private GroovyShell createShell(Map<String, Object> variables,
            PreHost prescript) throws AppException {
        /*
         * CompilerConfiguration compiler = new CompilerConfiguration(); if
         * (prescript != null) { try { prescript.configureCompiler(compiler); }
         * catch (Exception e) { throw log.errorConfigureCompiler(e, profile); }
         * } compiler.setScriptBaseClass(LinuxScript.class.getName());
         * ClassLoader classLoader = getClass().getClassLoader(); Binding
         * binding = createBinding(variables, profile); return new
         * GroovyShell(classLoader, binding, compiler);
         */
        return null;
    }

}
