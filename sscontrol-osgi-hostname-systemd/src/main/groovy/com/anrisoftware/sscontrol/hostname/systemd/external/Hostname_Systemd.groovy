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
package com.anrisoftware.sscontrol.hostname.systemd.external

import groovy.util.logging.Slf4j

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.hostname.external.Hostname

/**
 * Configures the <i>hostname</i> service systems that use systemd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Hostname_Systemd extends ScriptBase {

    def restartService() {
        log.info 'Restarting hostname service.'
        shell privileged: true, "hostnamectl set-hostname $service.hostname" call()
    }

    @Override
    Hostname getService() {
        super.getService();
    }

    @Override
    def getLog() {
        log
    }
}
