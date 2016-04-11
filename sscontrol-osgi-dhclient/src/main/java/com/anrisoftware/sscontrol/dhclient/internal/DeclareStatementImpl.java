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

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.dhclient.external.DeclareStatement;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Database script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class DeclareStatementImpl extends AbstractDeclaration implements
        DeclareStatement {

    public interface DeclareStatementImplFactory {

        DeclareStatementImpl create();

        DeclareStatementImpl create(@Assisted DeclareStatement declare);

    }

    private String name;

    private String value;

    @AssistedInject
    DeclareStatementImpl() {
        super();
    }

    @AssistedInject
    DeclareStatementImpl(@Assisted DeclareStatement declare) {
        super(declare.getStatements());
    }

    public DeclareStatement declare(String name) {
        this.name = name;
        return this;
    }

    public DeclareStatement declare(String name, String value) {
        this.name = name;
        this.value = value;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name)
                .append("value", value).appendSuper(super.toString())
                .toString();
    }
}
