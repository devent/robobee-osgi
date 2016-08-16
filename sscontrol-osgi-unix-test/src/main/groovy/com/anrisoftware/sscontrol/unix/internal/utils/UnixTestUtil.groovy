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

    static String fileToString(File file) {
        String str = FileUtils.readFileToString file
        str.replaceAll(/junit\d+/, 'junit')
    }

    static String resourceToString(URL resource) {
        IOUtils.toString resource
    }

    static final URL echoCommand = UnixTestUtil.class.getResource('echo_command.txt')
}
