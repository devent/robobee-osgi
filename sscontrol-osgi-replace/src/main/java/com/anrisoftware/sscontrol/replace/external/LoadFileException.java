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

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import com.anrisoftware.sscontrol.types.external.AppException;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class LoadFileException extends AppException {

    public LoadFileException(URL dest, Charset charset, IOException e) {
        super("Load file error", e);
        addContextValue("dest", dest);
        addContextValue("charset", charset);
    }

}
