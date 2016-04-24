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

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.profile.external.ProfileProperties
import com.anrisoftware.sscontrol.types.external.SscontrolScript

/**
 * Base of all scripts that are using the Groovy syntax and compiler.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
abstract class LinuxScript extends Script {

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
     * Returns the default properties for the service, as in example:
     *
     * <pre>
     * ---
     * &#64;Inject
     * &#64;Named("my-properties")
     * ContextProperties myProperties
     *
     * &#64;Override
     * ContextProperties getDefaultProperties() {
     *     myProperties
     * }
     * ---
     * </pre>
     */
    abstract ContextProperties getDefaultProperties()

    /**
     * Returns the service of the script.
     *
     * @return the {@link SscontrolScript}.
     */
    SscontrolScript getService() {
        service
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

    @Override
    String toString() {
        new ToStringBuilder(this).append(getName())
    }
}
