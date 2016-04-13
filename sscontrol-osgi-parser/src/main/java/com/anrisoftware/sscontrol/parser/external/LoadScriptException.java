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
package com.anrisoftware.sscontrol.parser.external;

import java.net.URI;

import com.anrisoftware.sscontrol.types.external.AppException;

@SuppressWarnings("serial")
public class LoadScriptException extends AppException {

    public LoadScriptException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadScriptException(String message) {
        super(message);
    }

    public LoadScriptException(Parser parser, Exception e, String name, URI root) {
        super("Load script error", e);
        addContextValue("name", name);
        addContextValue("root", root);
        addContextValue("parser", parser);
    }

}
