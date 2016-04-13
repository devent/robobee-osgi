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
package com.anrisoftware.sscontrol.parser.groovy.internal;

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

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.anrisoftware.sscontrol.parser.external.LoadScriptException;
import com.anrisoftware.sscontrol.parser.external.Parser;
import com.anrisoftware.sscontrol.parser.groovy.external.ScriptServiceNotFound;
import com.anrisoftware.sscontrol.types.external.SscontrolScript;
import com.anrisoftware.sscontrol.types.external.SscontrolScriptService;

/**
 * Groovy script parser.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ParserImpl implements Parser {

    public interface ParserImplFactory {

        ParserImpl create();

    }

    @Inject
    private ParserImplLogger log;

    @Inject
    private BundleContext bundleContext;

    @Override
    public SscontrolScript parse(URI resource) throws LoadScriptException {
        String scriptName = FilenameUtils.getName(resource.getPath());
        URI parent = getParent(resource);
        String scriptServiceName = loadScriptName(scriptName, parent);
        SscontrolScriptService scriptService = loadScriptService(scriptServiceName);
        SscontrolScript script = scriptService.create();
        SscontrolScript parsedScript = loadScript(scriptName,
                scriptServiceName, parent, script);
        log.parsedScript(this, parsedScript);
        return parsedScript;
    }

    private SscontrolScriptService loadScriptService(String scriptName)
            throws ScriptServiceNotFound {
        String serviceName = format(
                "com.anrisoftware.sscontrol.%s.external.%s%s", scriptName,
                capitalize(scriptName), "Service");
        ServiceReference<?> reference = bundleContext
                .getServiceReference(serviceName);
        if (reference == null) {
            throw new ScriptServiceNotFound(this, serviceName);
        }
        return (SscontrolScriptService) bundleContext.getService(reference);
    }

    private String loadScriptName(String name, URI parent)
            throws LoadScriptException {
        try {
            loadScript(name, null, parent, null);
            throw new IllegalStateException();
        } catch (MissingPropertyException e) {
            return e.getProperty();
        }
    }

    private SscontrolScript loadScript(String name, String serviceName,
            URI parent, SscontrolScript script) throws LoadScriptException {
        try {
            CompilerConfiguration cc = new CompilerConfiguration();
            cc.setScriptBaseClass(ParsedScript.class.getName());
            GroovyScriptEngine engine = new GroovyScriptEngine(
                    new URL[] { parent.toURL() });
            Binding binding = new Binding();
            if (serviceName != null) {
                binding.setProperty(serviceName, script);
            }
            engine.run(name, binding);
            return (SscontrolScript) binding.getProperty(serviceName);
        } catch (MalformedURLException e) {
            throw new LoadScriptException(this, e, name, parent);
        } catch (ResourceException e) {
            throw new LoadScriptException(this, e, name, parent);
        } catch (ScriptException e) {
            throw new LoadScriptException(this, e, name, parent);
        }
    }

    private URI getParent(URI resource) {
        return resource.getPath().endsWith("/") ? resource.resolve("..")
                : resource.resolve(".");
    }

}
