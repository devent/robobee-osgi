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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.codehaus.groovy.control.CompilerConfiguration;

import com.anrisoftware.sscontrol.parser.external.LoadScriptException;
import com.anrisoftware.sscontrol.parser.external.Parser;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.HostServices;
import com.anrisoftware.sscontrol.types.external.PreHost;
import com.google.inject.assistedinject.Assisted;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

/**
 * Groovy script parser.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ParserImpl implements Parser {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ParserImplFactory {

        ParserImpl create(@Assisted URI[] roots, @Assisted String name,
                @Assisted Map<String, Object> variables,
                @Assisted HostServices hostServices);

    }

    private final Map<String, Object> variables;

    private final HostServices hostServices;

    private final URI[] roots;

    private final String name;

    @Inject
    ParserImpl(@Assisted URI[] roots, @Assisted String name,
            @Assisted Map<String, Object> variables,
            @Assisted HostServices hostServices) {
        this.hostServices = hostServices;
        this.roots = roots.clone();
        this.name = name;
        this.variables = new HashMap<String, Object>(variables);
    }

    @Override
    public HostServices parse() throws AppException {
        return parseScript();
    }

    private HostServices parseScript() throws AppException {
        URL[] roots = toURLs(this.roots);
        return loadScript(roots);
    }

    private HostServices loadScript(URL[] roots) throws AppException {
        CompilerConfiguration cc = createCompiler();
        Binding binding = createBinding();
        GroovyScriptEngine engine = createEngine(cc, binding, roots);
        try {
            engine.run(name, binding);
            return hostServices;
        } catch (ResourceException e) {
            throw new LoadScriptException(this, e, name);
        } catch (ScriptException e) {
            throw new LoadScriptException(this, e, name);
        }
    }

    private GroovyScriptEngine createEngine(CompilerConfiguration cc,
            Binding binding, URL[] roots) throws AppException {
        GroovyScriptEngine engine = new GroovyScriptEngine(roots);
        engine.setConfig(cc);
        return engine;
    }

    private CompilerConfiguration createCompiler() throws AppException {
        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass(ParsedScript.class.getName());
        Set<String> services = hostServices.getAvailableServices();
        for (String name : services) {
            PreHost pre = hostServices.getAvailablePreService(name).create();
            pre.configureCompiler(cc);
        }
        return cc;
    }

    private Binding createBinding() {
        Binding binding = new Binding();
        binding.setProperty("service", hostServices);
        binding.setProperty("targets", hostServices.getTargets());
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            binding.setProperty(entry.getKey(), entry.getValue());
        }
        return binding;
    }

    private URL[] toURLs(URI[] roots) throws LoadScriptException {
        URL[] urls = new URL[roots.length];
        for (int i = 0; i < urls.length; i++) {
            urls[i] = toURL(roots[i]);
        }
        return urls;
    }

    private URL toURL(URI uri) throws LoadScriptException {
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            throw new LoadScriptException(this, e, name);
        }
    }

}
