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
package com.anrisoftware.sscontrol.database.internal;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.database.external.DatabaseDb;
import com.google.inject.assistedinject.Assisted;

/**
 * Database.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class DatabaseDbImpl implements DatabaseDb {

    public interface DatabaseDbImplFactory {

        DatabaseDbImpl create(Map<String, Object> args);

    }

    private final String name;

    @Inject
    DatabaseDbImpl(@Assisted Map<String, Object> args) {
        this.name = toName(args.get("name"));
    }

    @SuppressWarnings("deprecation")
    private String toName(Object object) {
        notNull(object, "name=null");
        return ObjectUtils.toString(object);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name).toString();
    }
}
