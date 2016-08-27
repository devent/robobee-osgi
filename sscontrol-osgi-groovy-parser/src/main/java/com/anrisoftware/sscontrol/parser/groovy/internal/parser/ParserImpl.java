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
package com.anrisoftware.sscontrol.parser.groovy.internal.parser;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.capitalize;
import groovy.lang.Binding;
import groovy.lang.MissingPropertyException;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.anrisoftware.sscontrol.parser.external.LoadScriptException;
import com.anrisoftware.sscontrol.parser.external.Parser;
import com.anrisoftware.sscontrol.parser.groovy.external.ScriptServiceNotFound;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.PreHost;
import com.anrisoftware.sscontrol.types.external.PreHostService;
import com.anrisoftware.sscontrol.types.external.HostService;
import com.anrisoftware.sscontrol.types.external.HostServiceService;
import com.google.inject.assistedinject.Assisted;

/**
 * Groovy script parser.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ParserImpl implements Parser {

    public interface ParserImplFactory {

        ParserImpl create(@Assisted Map<String, Object> variables);

    }

    private final Map<String, Object> variables;

    @Inject
    private ParserImplLogger log;

    @Inject
    private BundleContext bundleContext;

    @Inject
    ParserImpl(@Assisted Map<String, Object> variables) {
        this.variables = new HashMap<String, Object>(variables);
    }

    @Override
    public HostService parse(URI resource) throws AppException {
        String scriptName = FilenameUtils.getName(resource.getPath());
        URI parent = getParent(resource);
        HostServiceService scriptService;
        PreHostService prescript;
        String scriptServiceName = parseName(resource);
        scriptService = getService(scriptServiceName, "Service");
        prescript = getService(scriptServiceName, "PreScriptService");
        HostService script = scriptService.create();
        HostService parsedScript = loadScript(scriptName,
                scriptServiceName, parent, script, prescript.create());
        log.parsedScript(this, parsedScript);
        return parsedScript;
    }

    private String parseName(URI resource) throws AppException {
        String scriptName = FilenameUtils.getName(resource.getPath());
        URI parent = getParent(resource);
        String scriptServiceName = loadScriptName(scriptName, parent, null);
        return scriptServiceName;
    }

    @SuppressWarnings("unchecked")
    private <T> T getService(String name, String postfix)
            throws ScriptServiceNotFound {
        String serviceName = format(
                "com.anrisoftware.sscontrol.%s.external.%s%s", name,
                capitalize(name), postfix);
        ServiceReference<?> reference = bundleContext
                .getServiceReference(serviceName);
        if (reference == null) {
            throw new ScriptServiceNotFound(this, serviceName);
        }
        return (T) bundleContext.getService(reference);
    }

    private String loadScriptName(String name, URI parent,
            PreHost prescript) throws AppException {
        try {
            loadScript(name, null, parent, null, prescript);
            throw new IllegalStateException();
        } catch (MissingPropertyException e) {
            return e.getProperty();
        }
    }

    private HostService loadScript(String name, String serviceName,
            URI parent, HostService script, PreHost prescript)
            throws AppException {
        CompilerConfiguration cc = createCompiler(prescript);
        Binding binding = createBinding();
        GroovyScriptEngine engine = createEngine(name, serviceName, parent,
                script, cc, binding);
        try {
            engine.run(name, binding);
            return (HostService) binding.getProperty(serviceName);
        } catch (ResourceException e) {
            throw new LoadScriptException(this, e, name, parent);
        } catch (ScriptException e) {
            throw new LoadScriptException(this, e, name, parent);
        }
    }

    private GroovyScriptEngine createEngine(String name, String serviceName,
            URI parent, HostService script, CompilerConfiguration cc,
            Binding binding) throws AppException {
        try {
            GroovyScriptEngine engine = new GroovyScriptEngine(
                    new URL[] { parent.toURL() });
            engine.setConfig(cc);
            if (serviceName != null) {
                binding.setProperty(serviceName, script);
            }
            return engine;
        } catch (MalformedURLException e) {
            throw new LoadScriptException(this, e, name, parent);
        }
    }

    private CompilerConfiguration createCompiler(PreHost prescript)
            throws AppException {
        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass(ParsedScript.class.getName());
        if (prescript != null) {
            prescript.configureCompiler(cc);
        }
        return cc;
    }

    private Binding createBinding() {
        Binding binding = new Binding();
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            binding.setProperty(entry.getKey(), entry.getValue());
        }
        return binding;
    }

    private URI getParent(URI resource) {
        return resource.getPath().endsWith("/") ? resource.resolve("..")
                : resource.resolve(".");
    }

}
