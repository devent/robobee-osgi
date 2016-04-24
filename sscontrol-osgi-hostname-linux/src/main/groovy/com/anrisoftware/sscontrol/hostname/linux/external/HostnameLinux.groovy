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
package com.anrisoftware.sscontrol.hostname.linux.external

import com.anrisoftware.sscontrol.groovy.script.external.LinuxScript

/**
 * Configures the <i>hostname</i> service for GNU/Linux systems.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
abstract class HostnameLinux extends LinuxScript {

    @Override
    def run() {
        distributionSpecificConfiguration()
        deployHostnameConfiguration()
        restartService()
    }

    /**
     * Do some distribution specific configuration.
     */
    abstract void distributionSpecificConfiguration()

    /**
     * Deploys the <i>hostname</i> configuration.
     */
    void deployHostnameConfiguration() {
    }

    /**
     * Restarts the <i>hostname</i> service.
     */
    void restartService() {
    }

    /**
     * Returns the current <i>hostname</i> configuration.
     */
    String getCurrentHostnameConfiguration() {
    }

    /**
     * Returns the <i>hostname</i> configuration.
     */
    List getHostnameConfiguration() {
        []
    }

    /**
     * Returns the <i>hostname</i> configuration file, for
     * example {@code "hostname".} If the file path is not absolute then the
     * file is assumed under the configuration directory.
     *
     * <ul>
     * <li>property key {@code configuration_file}</li>
     * </ul>
     */
    File getHostnameFile() {
        profile.getFileProperty "configuration_file", configurationDir, defaultProperties
    }

    /**
     * Returns the <i>hostname</i> configuration file, for
     * example {@code "/etc".}
     *
     * <ul>
     * <li>property key {@code configuration_file}</li>
     * </ul>
     */
    File getConfigurationFile() {
        profile.getFileProperty "configuration_directory", defaultProperties
    }
}
