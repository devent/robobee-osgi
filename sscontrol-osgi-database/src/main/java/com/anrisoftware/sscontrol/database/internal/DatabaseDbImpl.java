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

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.database.external.DatabaseDb;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.ToStringService;

/**
 * Database.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class DatabaseDbImpl implements DatabaseDb {

    public interface DatabaseDbImplFactory {

        DatabaseDbImpl create();

    }

    @Inject
    private ToStringService toString;

    private String name;

    private String charset;

    private String collate;

    public DatabaseDb db(Map<String, Object> args) throws AppException {
        this.name = toString.toString(args, "name");
        if (args.containsKey("charset")) {
            this.charset = toString.toString(args, "charset");
            this.collate = toString.toString(args, "collate");
        }
        return this;
    }

    public void script(String text) {

    }

    public void script(Map<String, Object> args) {

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCharset() {
        return charset;
    }

    @Override
    public String getCollate() {
        return collate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name).toString();
    }
}
