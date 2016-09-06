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
import java.util.List;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import com.anrisoftware.sscontrol.parser.external.Parser;
import com.anrisoftware.sscontrol.parser.external.ParserService;
import com.anrisoftware.sscontrol.types.external.HostServices;
import com.anrisoftware.sscontrol.types.external.HostServicesService;

/**
 * Karaf {@code robobee:parse} command.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Command(scope = "robobee", name = "parse", description = "Parses the specified resource and checks for eventual errors.")
@Service
public class ParseCommand implements Action {

    @Argument(name = "roots", description = "The resource roots.", required = true, multiValued = true)
    private List<String> roots;

    @Argument(name = "name", description = "The resource to be parsed.", required = true, multiValued = false)
    private String name;

    @Reference
    private ParserService parseService;

    @Reference
    private HostServicesService scriptsRepositoryService;

    @Override
    public Object execute() throws Exception {
        URI[] uriroots = toUris(roots);
        HostServices hostServices = scriptsRepositoryService.create();
        Parser parser = parseService.create(uriroots, name, hostServices);
        parser.parse();
        return format("%s parsed.", name);
    }

    private URI[] toUris(List<String> roots) throws URISyntaxException {
        URI[] uris = new URI[roots.size()];
        for (int i = 0; i < uris.length; i++) {
            uris[i] = toUri(roots.get(i));
        }
        return null;
    }

    private URI toUri(String resource) throws URISyntaxException {
        return new URI(resource);
    }
}
