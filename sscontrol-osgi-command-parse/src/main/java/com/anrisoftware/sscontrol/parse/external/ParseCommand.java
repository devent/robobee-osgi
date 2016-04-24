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

package com.anrisoftware.sscontrol.parse.external;

import static java.lang.String.format;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import com.anrisoftware.sscontrol.parser.external.Parser;
import com.anrisoftware.sscontrol.parser.external.ParserService;
import com.anrisoftware.sscontrol.scripts.external.ScriptsRepository;
import com.anrisoftware.sscontrol.scripts.external.ScriptsRepositoryService;
import com.anrisoftware.sscontrol.types.external.SscontrolScript;

/**
 * Karaf {@code sscontrol:parse} command.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Command(scope = "sscontrol", name = "parse", description = "Parses the specified resource and checks for eventual errors.")
@Service
public class ParseCommand implements Action {

    @Argument(name = "resource", description = "The resource URI to be parsed.", required = true, multiValued = false)
    private String resource;

    @Reference
    private ParserService parseService;

    @Reference
    private ScriptsRepositoryService scriptsRepositoryService;

    @Override
    public Object execute() throws Exception {
        Parser parser = parseService.create();
        SscontrolScript script = parser.parse(toUri(resource));
        ScriptsRepository scriptsRepository = scriptsRepositoryService.create();
        scriptsRepository.putScript(script.getClass().getName(), script);
        return format("%s added.", script.getClass().getName());
    }

    private URI toUri(String resource) throws URISyntaxException {
        return new URI(resource);
    }
}
