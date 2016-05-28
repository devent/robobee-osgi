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
package com.anrisoftware.sscontrol.debug.external;

import java.util.Map;

/**
 * <p>
 * Debug logging.
 * </p>
 * <h2>Usage Example</h2>
 * 
 * <pre>
 * public void debug(Map<String, Object> args, String name) {
 *     Map<String, Object> arguments = new HashMap<String, Object>(args);
 *     arguments.put("name", name);
 *     invokeMethod(debug, "debug", arguments);
 * }
 * 
 * public void debug(Map<String, Object> args) {
 *     Map<String, Object> arguments = new HashMap<String, Object>(args);
 *     invokeMethod(debug, "debug", arguments);
 * }
 * 
 * &#64;SuppressWarnings("unchecked")
 * public List<Object> getDebug() {
 *     return (List<Object>) invokeMethod(debug, "getDebug", null);
 * }
 * 
 * </pre>
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface DebugLogging {

    /**
     * Returns the modules for which the logging is active.
     */
    Map<String, DebugModule> getModules();

    /**
     * Adds the specified logging module.
     */
    DebugLogging putModule(DebugModule module);

    /**
     * Removes the specified logging module.
     */
    DebugLogging removeModule(String name);

}
