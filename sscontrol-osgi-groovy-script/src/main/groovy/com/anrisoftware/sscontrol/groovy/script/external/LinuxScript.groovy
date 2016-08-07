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
package com.anrisoftware.sscontrol.groovy.script.external

import groovy.util.logging.Slf4j

import java.util.concurrent.ExecutorService

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.sscontrol.types.external.ProfileProperties
import com.anrisoftware.sscontrol.types.external.ScriptsRepository
import com.anrisoftware.sscontrol.types.external.SscontrolScript
import com.anrisoftware.sscontrol.types.external.SscontrolServiceScript

/**
 * Base of all scripts that are using the Groovy syntax and compiler.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class LinuxScript extends Script implements SscontrolServiceScript {

    /**
     * The {@link String} name of the script.
     */
    String name

    /**
     * The service {@link ProfileProperties} profile properties.
     */
    ProfileProperties profile

    /**
     * The {@link SscontrolService} of the script.
     */
    SscontrolScript service

    /**
     * The {@link ExecutorService} pool to run the scripts on.
     */
    ExecutorService threads

    /**
     * The {@link ScriptsRepository} containing the scripts.
     */
    ScriptsRepository scriptsRepository

    @Override
    public <T extends ExecutorService> T getThreads() {
        threads
    }

    /**
     * Returns the system name, for example {@code "ubuntu"}.
     *
     * <ul>
     * <li>profile property {@code system_name}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getSystemName() {
        profile.getProperty "system_name", defaultProperties
    }

    /**
     * Returns the distribution name, for example {@code "lucid"}.
     *
     * <ul>
     * <li>profile property {@code distribution_name}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDistributionName() {
        profile.getProperty "distribution_name", defaultProperties
    }

    /**
     * Returns the repository string, for
     * example {@code "deb http://archive.ubuntu.com/ubuntu <distributionName> <repository>"}
     *
     * <ul>
     * <li>profile property {@code repository_string}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getRepositoryString() {
        profile.getProperty "repository_string", defaultProperties
    }

    /**
     * Returns the needed packages of the service.
     *
     * <ul>
     * <li>profile property {@code packages}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getPackages() {
        profile.getListProperty "packages", defaultProperties
    }

    def getLog() {
        log
    }

    @Override
    String toString() {
        new ToStringBuilder(this).append(getName())
    }
}
