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
package com.anrisoftware.sscontrol.shell.external.utils

import groovy.transform.CompileStatic

import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils


/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@CompileStatic
class UnixTestUtil {

    static void createEchoCommands(File dir, List<String> names) {
        names.each { createEchoCommand dir, it }
    }

    static String createEchoCommand(File dir, String name) {
        def file = new File(dir, name)
        def stream = new FileOutputStream(file)
        IOUtils.copy echoCommand.openStream(), stream
        stream.close()
        file.setExecutable true
        return file.absolutePath
    }

    static void createBashCommand(File dir) {
        def bash = new File("/bin/bash")
        def bashDest = new File(dir, "bash")
        def out = new FileOutputStream(bashDest)
        if (bash.isFile()) {
            IOUtils.copy new FileInputStream(bash), out
        }
        bashDest.setExecutable true
    }

    static String fileToString(File file) {
        String str = FileUtils.readFileToString file
        str.replaceAll(/junit\d+/, 'junit')
    }

    static String resourceToString(URL resource) {
        IOUtils.toString resource
    }

    static final URL echoCommand = UnixTestUtil.class.getResource('echo_command.txt')

    static final URL robobeeKey = UnixTestUtil.class.getResource('robobee')
}
