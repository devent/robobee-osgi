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

import com.anrisoftware.sscontrol.dhclient.external.OptionDeclaration;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Option to add or to remove.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class OptionDeclarationImpl implements OptionDeclaration {

    public interface OptionDeclarationImplFactory {

        OptionDeclarationImpl create();

        OptionDeclarationImpl create(@Assisted OptionDeclaration option);

    }

    private String option;

    private boolean remove;

    @AssistedInject
    OptionDeclarationImpl() {
    }

    @AssistedInject
    OptionDeclarationImpl(@Assisted OptionDeclaration option) {
        this.option = option.getOption();
        this.remove = option.isRemove();
    }

    public void option(String option) {
        if (option.startsWith("!")) {
            this.remove = true;
            option = option.substring(1);
        } else {
            this.remove = false;
        }
        this.option = option;
    }

    @Override
    public String getOption() {
        return option;
    }

    @Override
    public boolean isRemove() {
        return remove;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(option).toString();
    }

}
