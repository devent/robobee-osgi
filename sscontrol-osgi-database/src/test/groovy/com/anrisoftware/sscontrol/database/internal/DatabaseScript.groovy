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
package com.anrisoftware.sscontrol.database.internal

def databasemap = [
    server: [host: '127.0.0.1', port: 3306],
    password: 'somepass',
    phpmyadmin: [
        name: 'phpmyadmin',
        password: 'somepass',
        db: 'phpmyadmindb',
    ],
    wordpressdb: [
        name: 'wordpressdb',
        password: 'somepass',
        db: 'wordpressdb',
    ],
]

database.with {
    bind databasemap.server.host, port: databasemap.server.port
    admin user: 'root', password: databasemap.password
    db name: databasemap.phpmyadmin.db
    user databasemap.phpmyadmin.name, password: databasemap.phpmyadmin.password with {
        access database: databasemap.phpmyadmin.db
    }
}

database.with {
    user databasemap.wordpressdb with {
        access database: databasemap.phpmyadmin.db
    }
}

database.with {
    debug "general", level: 1, file: "/var/log/mysql/mysql.log"
}

debugLogs = []
debugLogs << [name: "error", level: 1]
debugLogs << [name: "slow-queries", level: 1]

debugLogs.each { database.debug it }
