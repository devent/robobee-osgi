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
package com.anrisoftware.sscontrol.itests.karaf;

import static org.apache.commons.lang3.StringUtils.*
import static org.ops4j.pax.exam.CoreOptions.*
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.*
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit

import org.apache.karaf.shell.api.console.Session
import org.apache.karaf.shell.api.console.SessionFactory


@CompileStatic
@Slf4j
class ExecuteCommandKaraf {

    SessionFactory sessionFactory

    Session session;

    ExecutorService executor

    ByteArrayOutputStream byteArrayOutputStream

    def PrintStream printStream

    def PrintStream errStream

    void build() {
        this.byteArrayOutputStream = new ByteArrayOutputStream()
        this.printStream = new PrintStream(byteArrayOutputStream)
        this.errStream = new PrintStream(byteArrayOutputStream)
        this.executor = Executors.newCachedThreadPool()
        this.session = sessionFactory.create(System.in, printStream, errStream);
    }

    String[] "list features"() {
        def features = split executeCommand('feature:list'), '\n'
        log.info 'Discovered features: {}', join(features, '\n')
        return features
    }

    String executeCommand(String command) {
        byteArrayOutputStream.flush()
        byteArrayOutputStream.reset()
        String response;
        def commandFuture = new FutureTask<String>({
            try {
                System.err.println(command);
                session.execute(command);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
            printStream.flush();
            errStream.flush();
            return byteArrayOutputStream.toString();
        })

        try {
            executor.submit(commandFuture);
            response = commandFuture.get(10000L, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            response = "SHELL COMMAND TIMED OUT: ";
        }

        System.err.println(response);

        return response;
    }
}
