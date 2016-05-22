/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-scripts-unix.
 *
 * sscontrol-scripts-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-scripts-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-scripts-unix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.unix.internal.core;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.util.Providers.of;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.exec.external.runcommands.RunCommandsFactory;
import com.anrisoftware.globalpom.exec.external.runcommands.RunCommandsService;
import com.anrisoftware.globalpom.exec.external.scriptprocess.ScriptExecService;
import com.anrisoftware.globalpom.exec.internal.runcommands.RunCommands;
import com.anrisoftware.globalpom.exec.internal.runcommands.RunCommandsArg;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.propertiesutils.ContextPropertiesService;
import com.anrisoftware.resources.templates.external.TemplateResource;
import com.anrisoftware.resources.templates.external.TemplatesFactory;
import com.anrisoftware.resources.templates.external.TemplatesService;
import com.anrisoftware.sscontrol.unix.external.core.Cmd;
import com.anrisoftware.sscontrol.unix.internal.core.ArgumentParser.ArgumentParserFactory;
import com.google.inject.AbstractModule;

/**
 * Runs the specified command.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Component
@Service(Cmd.class)
public class CmdImpl implements Cmd {

    @Inject
    private CmdLogger log;

    @Reference
    private ScriptExecService scriptExecService;

    @Reference
    private RunCommandsService runCommandsService;

    @Reference
    private TemplatesService templatesService;

    @Reference
    private ContextPropertiesService contextProperties;

    @Inject
    private UnixTemplatesProvider templates;

    @Inject
    private RunCommandsArg runCommandsArg;

    @Inject
    private ArgumentParserFactory argumentParserFactory;

    @Override
    public ProcessTask call(Map<String, Object> args, String command,
            Object parent, Threads threads, Properties properties)
                    throws CommandExecException {
        notBlank(command, COMMAND_BLANK);
        notNull(parent, PARENT_NULL);
        notNull(threads, THREADS_NULL);
        ContextProperties cproperties;
        cproperties = contextProperties.create(parent, properties);
        return call0(new HashMap<String, Object>(args), command, parent,
                threads, cproperties);
    }

    private ProcessTask call0(Map<String, Object> args, String command,
            Object parent, Threads threads, ContextProperties properties)
                    throws CommandExecException {
        String commandKey = format("%s_command", command);
        String cmd = properties.getProperty(commandKey);
        notBlank(cmd, IS_BLANK, commandKey);
        args.put("command", cmd);
        String system = getSystem(args);
        String commandName = format(COMMAND_NAME_FORMAT, system,
                capitalize(command));
        RunCommands runCommands = runCommandsArg.runCommands(args, parent);
        TemplateResource res = templates.get().getResource(command);
        checkArgs(command, commandName, args);
        ProcessTask task = scriptExecService
                .create(args, parent, threads, res, commandName).call();
        runCommands.add(args.get(COMMAND_KEY), args);
        log.commandFinished(parent, task, args);
        return task;
    }

    private String getSystem(Map<String, Object> args) {
        Object arg = args.get("system");
        if (arg == null) {
            return "unix";
        }
        return args.get("system").toString();
    }

    private void checkArgs(String command, String commandName,
            Map<String, Object> args) throws CommandExecException {
        String argPropertiesStr = templates.get()
                .getResource(format(COMMAND_ARGS_FORMAT, command))
                .getText(commandName);
        argumentParserFactory.create(command, argPropertiesStr, args).parse();
    }

    @Activate
    protected void start() {
        createInjector(new CmdModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(RunCommandsFactory.class)
                        .toProvider(of(runCommandsService));
                bind(TemplatesFactory.class).toProvider(of(templatesService));
            }
        }).injectMembers(this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }

    private static final String COMMAND_NAME_FORMAT = "%s%s";

    private static final String THREADS_NULL = "threads = null";

    private static final String PARENT_NULL = "parent = null";

    private static final String COMMAND_BLANK = "command blank";

    private static final String IS_BLANK = "%s blank";

    private static final String COMMAND_KEY = "command";

    private static final String COMMAND_ARGS_FORMAT = "%s_args";

}
