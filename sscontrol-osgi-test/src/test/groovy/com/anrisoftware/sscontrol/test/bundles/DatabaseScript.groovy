/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-test.
 *
 * sscontrol-osgi-test is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-test is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-test. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.test.bundles

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
