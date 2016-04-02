/*
 * Copyright 2011-2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-bundle.
 *
 * sscontrol-bundle is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-bundle is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-bundle. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.loadscript;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;

@Command(scope = "robobee", name = "load", description = "Loads the specified script resource.")
@Service
public class LoadCommand implements Action {

    @Option(name = "-o", aliases = { "--option" }, description = "An option to the command", required = false, multiValued = false)
    private String option;

    @Argument(name = "argument", description = "Argument to the command", required = false, multiValued = false)
    private String argument;

    @Override
    public Object execute() throws Exception {
         System.out.println("Executing command load");
         System.out.println("Option: " + option);
         System.out.println("Argument: " + argument);
         return null;
    }
}
