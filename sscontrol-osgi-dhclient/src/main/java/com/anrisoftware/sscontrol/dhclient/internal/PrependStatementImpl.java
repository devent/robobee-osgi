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

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.strings.ToStringService;
import com.anrisoftware.sscontrol.dhclient.external.PrependStatement;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Prepend statement.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class PrependStatementImpl implements PrependStatement {

    public interface PrependStatementImplFactory {

        PrependStatementImpl create();

        PrependStatementImpl create(@Assisted PrependStatement prepend);

    }

    @Inject
    private ToStringService toString;

    private String option;

    private String value;

    @AssistedInject
    PrependStatementImpl() {
    }

    @AssistedInject
    PrependStatementImpl(@Assisted PrependStatement prepend) {
        this.option = prepend.getOption();
        this.value = prepend.getValue();
    }

    public void prepend(Map<String, Object> args) throws AppException {
        this.option = toString.toString(args, "option");
        this.value = toString.toString(args, "value");
    }

    @Override
    public String getOption() {
        return option;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("option", option)
                .append("value", value).toString();
    }

}
