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
package com.anrisoftware.sscontrol.replace.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.textmatch.match.MatchTextModule
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokensTemplateModule
import com.anrisoftware.sscontrol.replace.external.Replace.ReplaceFactory
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * 
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class ReplaceImplTest {

    @Inject
    ReplaceFactory replaceFactory

    @Test
    void "test replace"() {
        def testCases = [
            [
                search: /(?m)^define\('DB_NAME', '.*?'\);/,
                replace: "define('DB_NAME', 'db');",
                expected: '''
// ** MySQL settings - You can get this info from your web host ** //
/** The name of the database for WordPress */
define('DB_NAME', 'database_name_here');
''',
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            log.info '{}. case: {}', k, test
            def replace = replaceFactory.create(test)
            Eval.me 'replace', replace, test.input as String
            Closure expected = test.expected
            expected replace
        }
    }

    static Injector injector

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    @Before
    void setupTest() {
        injector.injectMembers(this)
    }

    @BeforeClass
    static void setupInjector() {
        toStringStyle
        this.injector = Guice.createInjector(
                new ReplaceModule(),
                new MatchTextModule(),
                new TokensTemplateModule(),
                new AbstractModule() {
                    protected void configure() {
                    }
                })
    }
}
