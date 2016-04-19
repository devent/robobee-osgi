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
package com.anrisoftware.sscontrol.types.external;

import static java.lang.String.format;

import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
public class ArgumentInvalidException extends AppException {

    public static void checkNullArg(Object arg, String name)
            throws ArgumentInvalidException {
        if (arg == null) {
            throw new ArgumentInvalidException(name, "null");
        }
    }

    public static void checkBlankArg(String arg, String name)
            throws ArgumentInvalidException {
        if (StringUtils.isBlank(arg)) {
            throw new ArgumentInvalidException(name, "blank");
        }
    }

    public ArgumentInvalidException(String name, String descr) {
        super(format("Argument %s", descr));
        addContextValue("argument", name);
    }

}
