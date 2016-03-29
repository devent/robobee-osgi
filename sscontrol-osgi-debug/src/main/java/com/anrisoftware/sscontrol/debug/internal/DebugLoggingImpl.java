/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-debug.
 *
 * sscontrol-osgi-debug is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-debug is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-debug. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.debug.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.debug.external.DebugLogging;
import com.anrisoftware.sscontrol.debug.external.DebugModule;
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

        DebugLoggingImpl create(@Assisted DebugLogging database);

    }

    private final Map<String, DebugModule> modules;

    @Inject
    private DebugImplLogger log;

    @Inject
    private DebugLoggingImplFactory debugFactory;

    @AssistedInject
    DebugLoggingImpl() {
        this.modules = new HashMap<String, DebugModule>();
    }

    @AssistedInject
    DebugLoggingImpl(@Assisted DebugLogging debug) {
        this.modules = new HashMap<String, DebugModule>(debug.getModules());
    }

    public void debug(Map<String, Object> args, String name) {

    }

    @Override
    public Map<String, DebugModule> getModules() {
        return Collections.unmodifiableMap(modules);
    }

    @Override
    public DebugLogging putModule(DebugModule module) {
        DebugLoggingImpl debug = debugFactory.create(this);
        debug.modules.put(module.getName(), module);
        return debug;
    }

    @Override
    public DebugLogging removeModule(String name) {
        DebugLoggingImpl debug = debugFactory.create(this);
        debug.modules.remove(name);
        return debug;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("modules", modules).toString();
    }
}
