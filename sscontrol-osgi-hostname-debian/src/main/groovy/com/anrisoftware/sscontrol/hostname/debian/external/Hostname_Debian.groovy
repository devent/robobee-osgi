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
package com.anrisoftware.sscontrol.hostname.debian.external

import groovy.util.logging.Slf4j

import com.anrisoftware.sscontrol.hostname.systemd.external.Hostname_Systemd

/**
 * Configures the <i>hostname</i> on Debian systems.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Hostname_Debian extends Hostname_Systemd {

    @Override
    def run() {
        installPackages()
        super.run()
    }

    /**
     * Installs the <i>hostname</i> packages.
     */
    void installPackages() {
        shell "apt-get install $packages.join(' ')" with { env << "DEBIAN_FRONTEND=noninteractive" }
    }

    @Override
    def getLog() {
        log
    }
}
