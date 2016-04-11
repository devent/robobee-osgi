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

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.sscontrol.dhclient.external.OptionDeclaration;
import com.anrisoftware.sscontrol.dhclient.external.OptionStatement;
import com.anrisoftware.sscontrol.dhclient.internal.OptionDeclarationImpl.OptionDeclarationImplFactory;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Option statement.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class OptionStatementImpl implements OptionStatement {

    public interface OptionStatementImplFactory {

        OptionStatementImpl create();

        OptionStatementImpl create(@Assisted OptionStatement option);

    }

    @Inject
    private OptionDeclarationImplFactory optionFactory;

    private OptionDeclaration option;

    @AssistedInject
    OptionStatementImpl() {
    }

    @AssistedInject
    OptionStatementImpl(@Assisted OptionStatement option) {
        this.option = option.getOption();
    }

    public void option(String string) {
        this.option = optionFactory.create();
        InvokerHelper.invokeMethodSafe(option, "option", string);
    }

    @Override
    public OptionDeclaration getOption() {
        return option;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(option).toString();
    }

}
