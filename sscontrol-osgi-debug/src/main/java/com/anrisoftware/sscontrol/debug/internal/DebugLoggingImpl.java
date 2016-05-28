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
package com.anrisoftware.sscontrol.debug.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.debug.external.DebugLogging;
import com.anrisoftware.sscontrol.debug.external.DebugModule;
import com.anrisoftware.sscontrol.debug.internal.DebugModuleImpl.DebugModuleImplFactory;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Debug logging.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class DebugLoggingImpl implements DebugLogging {

    public interface DebugLoggingImplFactory {

        DebugLoggingImpl create();

        DebugLoggingImpl create(@Assisted DebugLogging debug);

    }

    private final Map<String, DebugModule> modules;

    @Inject
    private DebugLoggingImplLogger log;

    @Inject
    private DebugLoggingImplFactory debugFactory;

    @Inject
    private DebugModuleImplFactory moduleFactory;

    @AssistedInject
    DebugLoggingImpl() {
        this.modules = new HashMap<String, DebugModule>();
    }

    @AssistedInject
    DebugLoggingImpl(@Assisted DebugLogging debug) {
        this.modules = new HashMap<String, DebugModule>(debug.getModules());
    }

    public void debug(Map<String, Object> args, String name) {
        DebugModuleImpl module = moduleFactory.create();
        module.debug(args, name);
        modules.put(name, module);
    }

    public void debug(Map<String, Object> args) throws AppException {
        DebugModuleImpl module = moduleFactory.create();
        module.debug(args);
        modules.put(module.getName(), module);
    }

    @SuppressWarnings("serial")
    public List<Object> getDebug() {
        return new ArrayList<Object>() {
            @Override
            public boolean add(Object e) {
                if (e instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> args = (Map<String, Object>) e;
                    DebugModuleImpl module = moduleFactory.create();
                    module.debug(args);
                    modules.put(module.getName(), module);
                }
                return super.add(e);
            }
        };
    }

    @Override
    public Map<String, DebugModule> getModules() {
        return Collections.unmodifiableMap(modules);
    }

    @Override
    public DebugLogging putModule(DebugModule module) {
        DebugLoggingImpl debug = debugFactory.create(this);
        debug.modules.put(module.getName(), module);
        log.modulePut(debug, module);
        return debug;
    }

    @Override
    public DebugLogging removeModule(String name) {
        DebugLoggingImpl debug = debugFactory.create(this);
        debug.modules.remove(name);
        log.moduleRemoved(debug, name);
        return debug;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("modules", modules).toString();
    }
}
