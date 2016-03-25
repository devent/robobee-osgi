/*
 * Copyright 2011-2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-database.
 *
 * sscontrol-osgi-database is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-database is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-database. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.database.internal

def databasemap = [
    server: '127.0.0.1',
    port: 3306,
    password: 'somepass',
    phpmyadmin: [
        user: 'phpmyadmin',
        password: 'somepass',
        db: 'phpmyadmindb',
    ],
]

database.with {
    bind databasemap.server, port: databasemap.port
    admin user: 'root', password: databasemap.password
    db name: databasemap.phpmyadmin.db
    user databasemap.phpmyadmin.user, password: databasemap.phpmyadmin.password with {
        access database: databasemap.phpmyadmin.db
    }
}
