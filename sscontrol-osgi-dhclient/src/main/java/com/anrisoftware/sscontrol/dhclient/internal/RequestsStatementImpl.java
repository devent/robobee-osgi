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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.globalpom.strings.ToStringService;
import com.anrisoftware.sscontrol.dhclient.external.OptionDeclaration;
import com.anrisoftware.sscontrol.dhclient.external.RequestsStatement;
import com.anrisoftware.sscontrol.dhclient.internal.OptionDeclarationImpl.OptionDeclarationImplFactory;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Collection of requests.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class RequestsStatementImpl implements RequestsStatement {

    public interface RequestsStatementImplFactory {

        RequestsStatementImpl create();

        RequestsStatementImpl create(@Assisted RequestsStatement requests);

    }

    private final List<OptionDeclaration> options;

    @Inject
    private RequestsStatementImplFactory requestsFactory;

    @Inject
    private OptionDeclarationImplFactory optionFactory;

    @Inject
    private ToStringService toString;

    @AssistedInject
    RequestsStatementImpl() {
        this.options = new ArrayList<OptionDeclaration>();
    }

    @AssistedInject
    RequestsStatementImpl(@Assisted RequestsStatement requests) {
        this.options = new ArrayList<OptionDeclaration>(requests.getRequests());
    }

    public void request(String option) throws AppException {
        String[] str = StringUtils.split(option, ",");
        request(Arrays.asList(str));
    }

    public void request(List<?> options) throws AppException {
        for (Object object : options) {
            String str = toString.toString(object, "option");
            OptionDeclarationImpl o = optionFactory.create();
            InvokerHelper.invokeMethodSafe(o, "option", str);
        }
    }

    @Override
    public RequestsStatement add(OptionDeclaration option) {
        RequestsStatementImpl r = requestsFactory.create(this);
        r.options.add(option);
        return r;
    }

    @Override
    public RequestsStatement remove(OptionDeclaration option) {
        RequestsStatementImpl r = requestsFactory.create(this);
        r.options.remove(option);
        return r;
    }

    @Override
    public List<OptionDeclaration> getRequests() {
        return Collections.unmodifiableList(options);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(options).toString();
    }

}
