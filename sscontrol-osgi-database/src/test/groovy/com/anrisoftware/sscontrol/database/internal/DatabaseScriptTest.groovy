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

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.google.inject.util.Providers.of
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.database.external.Database
import com.anrisoftware.sscontrol.database.external.DatabaseAccess
import com.anrisoftware.sscontrol.database.external.DatabaseDb
import com.anrisoftware.sscontrol.database.external.DatabaseUser
import com.anrisoftware.sscontrol.database.internal.DatabaseImpl.DatabaseImplFactory
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.types.external.BindingHostService
import com.anrisoftware.sscontrol.types.external.BindingHost.Host
import com.anrisoftware.sscontrol.types.groovy.internal.BindingHostServiceImpl
import com.anrisoftware.sscontrol.types.groovy.internal.GroovyTypesModule
import com.anrisoftware.sscontrol.types.internal.TypesModule
import com.google.inject.AbstractModule
import com.google.inject.Guice

@Slf4j
@CompileStatic
class DatabaseScriptTest {

    @Inject
    DatabaseImplFactory databaseFactory

    @Test
    void "database script"() {
        def testCases = [
            [
                input: """
database.with {
    binding 'localhost', port: 3306
}
database
""",
                expected: { Database database ->
                    assert database.binding.addedHosts.size() == 1
                    Host host = database.binding.addedHosts[0]
                    assert host.host == 'localhost'
                    assert host.port == 3306
                },
            ],
            [
                input: """
import static com.anrisoftware.sscontrol.types.external.BindingAddress.*
database.with {
    binding local, port: 3306
}
database
""",
                expected: { Database database ->
                    assert database.binding.addedHosts.size() == 1
                    Host host = database.binding.addedHosts[0]
                    assert host.host == '127.0.0.1'
                    assert host.port == 3306
                },
            ],
            [
                input: """
database.with {
    binding 'localhost', port: 3306
    admin user: 'root', password: 'somepass'
    db 'phpmyadmin'
    user 'phpmyadmin', password: '1234' with {
        access database: 'phpmyadmindb'
    }
}
database
""",
                expected: { Database database ->
                    assert database.binding.addedHosts.size() == 1
                    Host host = database.binding.addedHosts[0]
                    assert host.host == 'localhost'
                    assert host.port == 3306
                    assert database.adminUser.name == 'root'
                    assert database.adminUser.password == 'somepass'
                    assert database.databases.size() == 1
                    DatabaseDb db = database.databases[0]
                    assert db.name == 'phpmyadmin'
                    assert database.users.size() == 1
                    DatabaseUser user = database.users[0]
                    assert user.user.name == 'phpmyadmin'
                    assert user.user.password == '1234'
                    assert user.access.size() == 1
                    DatabaseAccess access = user.access[0]
                    assert access.database == 'phpmyadmindb'
                },
            ],
            [
                input: """
databasemap = [
    binding: [host: '127.0.0.1', port: 3306],
    admin: [user: 'root', password: 'somepass'],
    phpmyadmin: [user: [name: 'phpmyadmin', password: '1234'], db: [name: 'phpmyadmindb']],
]
database.with {
    binding databasemap.binding
    admin databasemap.admin
    db databasemap.phpmyadmin.db
    user databasemap.phpmyadmin.user with {
        access database: databasemap.phpmyadmin.db.name
    }
}
database
""",
                expected: { Database database ->
                    assert database.binding.addedHosts.size() == 1
                    Host host = database.binding.addedHosts[0]
                    assert host.host == '127.0.0.1'
                    assert host.port == 3306
                    assert database.adminUser.name == 'root'
                    assert database.adminUser.password == 'somepass'
                    assert database.databases.size() == 1
                    DatabaseDb db = database.databases[0]
                    assert db.name == 'phpmyadmindb'
                    assert database.users.size() == 1
                    DatabaseUser user = database.users[0]
                    assert user.user.name == 'phpmyadmin'
                    assert user.user.password == '1234'
                    assert user.access.size() == 1
                    DatabaseAccess access = user.access[0]
                    assert access.database == 'phpmyadmindb'
                },
            ],
            [
                input: """
database.with {
    db 'phpmyadmin' with {
        script \"\"\"
        SQL-SCRIPT
        \"\"\"
    }
}
database
""",
                expected: { Database database ->
                    assert database.databases.size() == 1
                    DatabaseDb db = database.databases[0]
                    assert db.name == 'phpmyadmin'
                },
            ],
            [
                input: """
wordpressdb = [name: 'wordpressdb', password: 'somepass', database: 'wordpressdb']
database.with {
    user wordpressdb with {
        access wordpressdb
    }
}
database
""",
                expected: { Database database ->
                    assert database.users.size() == 1
                    DatabaseUser user = database.users[0]
                    assert user.user.name == 'wordpressdb'
                    assert user.user.password == 'somepass'
                    assert user.access.size() == 1
                    DatabaseAccess access = user.access[0]
                    assert access.database == 'wordpressdb'
                },
            ],
            [
                input: """
database.with {
    debug "general", level: 1, file: "/var/log/mysql/mysql.log"
    debug "slow-queries", level: 1
}
database
""",
                expected: { Database database ->
                    assert database.debugLogging.modules.size() == 2
                },
            ],
            [
                input: """
database.with {
    debug << [name: "error", level: 1]
    debug << [name: "slow-queries", level: 1]
    debug << [name: "general", level: 1, file: "/var/log/mysql/mysql.log"]
}
database
""",
                expected: { Database database ->
                    assert database.debugLogging.modules.size() == 3
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            log.info '{}. case: {}', k, test
            def database = Eval.me 'database', databaseFactory.create(), test.input as String
            log.info '{}. case: database: {}', k, database
            Closure expected = test.expected
            expected database
        }
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new DatabaseModule(),
                new DebugLoggingModule(),
                new TypesModule(),
                new GroovyTypesModule(), new AbstractModule() {

                    @Override
                    protected void configure() {
                        bind BindingHostService to BindingHostServiceImpl
                    }
                }).injectMembers(this)
    }
}
