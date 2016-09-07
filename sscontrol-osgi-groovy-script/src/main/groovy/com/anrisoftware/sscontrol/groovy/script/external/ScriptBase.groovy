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

import com.anrisoftware.sscontrol.cmd.external.Shell
import com.anrisoftware.sscontrol.cmd.external.Shell.ShellFactory
import com.anrisoftware.sscontrol.types.external.HostService
import com.anrisoftware.sscontrol.types.external.HostServiceProperties
import com.anrisoftware.sscontrol.types.external.HostServiceScript
import com.anrisoftware.sscontrol.types.external.HostServices
import com.anrisoftware.sscontrol.types.external.Ssh
import com.anrisoftware.sscontrol.types.external.SshHost

/**
 * Base of all scripts that provides utilities functions and basic properties.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class ScriptBase extends Script implements HostServiceScript {

    /**
     * The {@link String} name of the script.
     */
    String name

    /**
     * The service {@link HostServiceProperties} properties.
     */
    HostServiceProperties properties

    /**
     * The {@link HostService} service.
     */
    HostService service

    /**
     * The {@link ExecutorService} pool to run the scripts on.
     */
    ExecutorService threads

    /**
     * The {@link HostServices} containing the services.
     */
    HostServices scriptsRepository

    /**
     * The command service to execute scripts.
     */
    ShellFactory shell

    /**
     * The hosts targets.
     */
    Ssh ssh

    @Override
    public <T extends ExecutorService> T getThreads() {
        threads
    }

    /**
     * Returns the lists of hosts.
     * 
     * @return the {@link List} of {@link SshHost} hosts.
     */
    List<SshHost> getHosts() {
        service.targets
    }

    /**
     * Executes the command.
     *
     * @param command the command to execute, can be multi-line.
     *
     * @return the process task of the executed command.
     */
    Shell shell(String command) {
        shell([:], command)
    }

    /**
     * Executes the command.
     *
     * @param args the arguments.
     *
     * @param command the command to execute, can be multi-line.
     *
     * @return the process task of the executed command.
     */
    Shell shell(Map args, String command) {
        if (ssh.targets.size() == 1) {
            shell.create(args, ssh.targets[0], this, threads, log, command)
        }
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
        properties.getProperty "repository_string", defaultProperties
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
        properties.getListProperty "packages", defaultProperties
    }

    def getLog() {
        log
    }

    @Override
    String toString() {
        new ToStringBuilder(this).append(getName())
    }
}
