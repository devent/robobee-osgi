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
package com.anrisoftware.sscontrol.dhclient.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.sscontrol.dhclient.external.Statement;
import com.anrisoftware.sscontrol.dhclient.internal.OptionStatementImpl.OptionStatementImplFactory;
import com.anrisoftware.sscontrol.dhclient.internal.PrependStatementImpl.PrependStatementImplFactory;
import com.anrisoftware.sscontrol.dhclient.internal.RequestsStatementImpl.RequestsStatementImplFactory;
import com.anrisoftware.sscontrol.dhclient.internal.SendStatementImpl.SendStatementImplFactory;

public class AbstractDeclaration {

    private final List<Statement> statements;

    @Inject
    private AbstractDeclarationLogger log;

    @Inject
    private OptionStatementImplFactory optionFactory;

    @Inject
    private SendStatementImplFactory sendFactory;

    @Inject
    private RequestsStatementImplFactory requestsFactory;

    @Inject
    private PrependStatementImplFactory prependFactory;

    protected AbstractDeclaration() {
        this.statements = new ArrayList<Statement>();
    }

    protected AbstractDeclaration(List<Statement> statements) {
        this.statements = new ArrayList<Statement>(statements);
    }

    public void option(String option) {
        OptionStatementImpl s = optionFactory.create();
        InvokerHelper.invokeMethodSafe(s, "option", option);
        addStatement(s);
    }

    public void send(String option) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("option", option);
        send(map);
    }

    public void send(String option, String value) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("option", option);
        map.put("value", value);
        send(map);
    }

    public void send(Map<String, Object> args) {
        Map<String, Object> map = new HashMap<String, Object>(args);
        SendStatementImpl s = sendFactory.create();
        InvokerHelper.invokeMethodSafe(s, "send", map);
        addStatement(s);
    }

    public void request(String option) {
        RequestsStatementImpl s = requestsFactory.create();
        InvokerHelper.invokeMethodSafe(s, "request", option);
        addStatement(s);
    }

    public void request(List<?> options) {
        RequestsStatementImpl s = requestsFactory.create();
        InvokerHelper.invokeMethodSafe(s, "request", options);
        addStatement(s);
    }

    public void prepend(String option, String value) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("option", option);
        map.put("value", value);
        PrependStatementImpl s = prependFactory.create();
        InvokerHelper.invokeMethodSafe(s, "prepend", map);
        addStatement(s);
    }

    protected void addStatement(Statement s) {
        statements.add(s);
        log.statementAdded(this, s);
    }

    public List<Statement> getStatements() {
        return Collections.unmodifiableList(statements);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("statements", statements)
                .toString();
    }

}
