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
package com.anrisoftware.sscontrol.profile.internal

profile.with {
    hostname
    hosts
    remote.with { service = 'openssh' }
    mail with {
        service = 'postfix'
        storage = 'mysql'
        auth = 'sasl'
        delivery = 'courier'
    }
    httpd with {
        service = ['idapache2': 'apache', 'idproxy': 'nginx']
    }
}
