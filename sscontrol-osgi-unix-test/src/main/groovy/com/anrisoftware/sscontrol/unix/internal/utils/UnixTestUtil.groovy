/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-unix-test.
 *
 * sscontrol-osgi-unix-test is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-unix-test is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-unix-test. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.unix.internal.utils

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
}
