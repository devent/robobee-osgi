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
package com.anrisoftware.sscontrol.replace.external;

import java.util.Map;

import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * Replace command.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Replace {

    static final String CHARSET_ARG = "charset";

    static final String REPLACE_ARG = "replace";

    static final String SEARCH_ARG = "search";

    static final String END_TOKEN_ARG = "endToken";

    static final String BEGIN_TOKEN_ARG = "beginToken";

    static final String DEST_ARG = "dest";

    /**
     * Temporary file name.
     */
    static final String TMP_ARG = "tmp";

    /**
     * Factory to create the replace command.
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ReplaceFactory {

        Replace create(@Assisted Map<String, Object> args,
                @Assisted SshHost host, @Assisted("parent") Object parent,
                @Assisted Threads threads, @Assisted("log") Object log);
    }

    /**
     * Executes the replacement.
     * 
     * @throws AppException
     */
    Replace call() throws AppException;

}
