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
package com.anrisoftware.sscontrol.dhclient.internal

dhclient.with {
    option 'rfc3442-classless-static-routes code 121 = array of unsigned integer 8'
    send 'host-name', 'andare.fugue.com';
    send 'host-name = gethostname()'
    request '!domain-name-servers'
    prepend 'domain-name-servers', '127.0.0.1'
    declare 'interface', 'eth0' with {
        // interface eth0
    }
    declare 'alias' with {
        // alias
    }
    declare 'lease' with {
        // lease
    }
}
