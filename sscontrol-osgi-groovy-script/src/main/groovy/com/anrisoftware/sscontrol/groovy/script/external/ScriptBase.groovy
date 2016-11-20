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

import static org.apache.commons.lang3.Validate.*
import groovy.util.logging.Slf4j

import java.util.concurrent.ExecutorService

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.sscontrol.copy.external.Copy
import com.anrisoftware.sscontrol.copy.external.Copy.CopyFactory
import com.anrisoftware.sscontrol.fetch.external.Fetch
import com.anrisoftware.sscontrol.fetch.external.Fetch.FetchFactory
import com.anrisoftware.sscontrol.replace.external.Replace
import com.anrisoftware.sscontrol.replace.external.Replace.ReplaceFactory
import com.anrisoftware.sscontrol.shell.external.Shell
import com.anrisoftware.sscontrol.shell.external.Shell.ShellFactory
import com.anrisoftware.sscontrol.types.external.HostService
import com.anrisoftware.sscontrol.types.external.HostServiceProperties
import com.anrisoftware.sscontrol.types.external.HostServiceScript
import com.anrisoftware.sscontrol.types.external.HostServices
import com.anrisoftware.sscontrol.types.external.SshHost
import com.google.inject.assistedinject.Assisted

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
     * The {@link HostService} service.
     */
    @Inject
    @Assisted
    HostService service

    /**
     * The {@link ExecutorService} pool to run the scripts on.
     */
    @Inject
    @Assisted
    ExecutorService threads

    /**
     * The {@link HostServices} containing the services.
     */
    @Inject
    @Assisted
    HostServices scriptsRepository

    /**
     * The hosts targets.
     */
    @Inject
    @Assisted
    SshHost target

    /**
     * Shell command.
     */
    @Inject
    ShellFactory shell

    /**
     * Fetch service.
     */
    @Inject
    FetchFactory fetch

    /**
     * Copy service.
     */
    @Inject
    CopyFactory copy

    /**
     * Replace service.
     */
    @Inject
    ReplaceFactory replace

    /**
     * The current working directory.
     */
    File pwd

    /**
     * The directory from where scripts are run.
     */
    File chdir

    /**
     * The directory from where sudo is run.
     */
    File sudoChdir

    /**
     * Environment variables.
     */
    Map env = [:]

    /**
     * Environment variables for sudo.
     */
    Map sudoEnv = [:]

    @Override
    public <T extends ExecutorService> T getThreads() {
        threads
    }

    @Override
    SshHost getTarget() {
        target
    }

    @Override
    def getLog() {
        log
    }

    @Override
    HostServiceProperties getProperties() {
        def p = service.serviceProperties
        notNull p, "serviceProperties=null for $service"
        return p
    }

    @Override
    String toString() {
        new ToStringBuilder(this).
                append('name', getName()).
                append('system', getSystemName()).
                append('version', getSystemVersion()).
                toString()
    }

    /**
     * Shell command.
     */
    Shell shell(String command) {
        shell([:], command)
    }

    /**
     * Shell command.
     */
    Shell shell(Map args, String command) {
        def a = setupArgs(args)
        shell.create(a, a.target, this, threads, log, command)
    }

    /**
     * Fetch command.
     */
    Fetch fetch(String src) {
        fetch([src: src])
    }

    /**
     * Fetch command.
     */
    Fetch fetch(Map args) {
        def a = setupArgs(args)
        fetch.create(a, a.target, this, threads, log)
    }

    /**
     * Copy command.
     */
    Copy copy(Map args, String src) {
        def a = new HashMap(args)
        a.src = src
        copy(a)
    }

    /**
     * Copy command.
     */
    Copy copy(Map args) {
        def a = setupArgs(args)
        copy.create(a, a.target, this, threads, log)
    }

    /**
     * Copy command.
     */
    Replace replace(Map args, String search) {
        def a = new HashMap(args)
        a.search = search
        replace(a)
    }

    /**
     * Replace command.
     */
    Replace replace(Map args) {
        def a = setupArgs(args)
        replace.create(a, a.target, this, threads, log)
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

    private setupArgs(Map args) {
        Map a = new HashMap(args)
        a = replaceMapValues env, a, "env"
        a.target = this.target
        if (args.containsKey('target')) {
            a.target = a.target
        }
        if (!a.containsKey('chdir')) {
            a.chdir = chdir
        }
        if (!a.containsKey('sudoChdir')) {
            a.sudoChdir = sudoChdir
        }
        if (!a.containsKey('pwd')) {
            a.pwd = pwd
        }
        if (!a.containsKey('sudoEnv')) {
            a.sudoEnv = sudoEnv
        } else {
            a.sudoEnv.putAll sudoEnv
        }
        return a
    }

    private replaceMapValues(Map replace, Map args, String key) {
        def map = new HashMap(replace)
        map.putAll(args[key] != null ? args[key] : [:])
        args[key] = map
        return args
    }
}
