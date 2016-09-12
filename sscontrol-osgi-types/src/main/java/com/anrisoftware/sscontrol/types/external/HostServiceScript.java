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
package com.anrisoftware.sscontrol.types.external;

import java.util.Properties;
import java.util.concurrent.ExecutorService;

/**
 * Host service script.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface HostServiceScript {

    /**
     * Executes the script.
     */
    Object run();

    /**
     * Returns the system name, for example {@code "ubuntu"}.
     */
    String getSystemName();

    /**
     * Returns the system version, for example {@code "14.04"}.
     */
    String getSystemVersion();

    /**
     * Returns the logger of the script.
     */
    Object getLog();

    /**
     * Returns service {@link HostServiceProperties} properties.
     */
    HostServiceProperties getProperties();

    /**
     * Returns {@link ExecutorService} pool to run the scripts on.
     */
    <T extends ExecutorService> T getThreads();

    /**
     * Returns the default {@link Properties} for the service.
     */
    Properties getDefaultProperties();

    /**
     * Returns the {@link HostService} script.
     */
    HostService getService();

    /**
     * Returns the {@link HostServiceScript} scripts.
     */
    HostServices getScriptsRepository();

    /**
     * Returns the {@link SshHost} target.
     */
    SshHost getTarget();
}
