package com.anrisoftware.sscontrol.unix.internal.core;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.Validate.notBlank;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.exec.external.scriptprocess.ScriptExecFactory;
import com.anrisoftware.globalpom.exec.internal.runcommands.RunCommands;
import com.anrisoftware.globalpom.exec.internal.runcommands.RunCommandsArg;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.resources.templates.external.TemplateResource;
import com.anrisoftware.sscontrol.types.external.ProfileProperties;
import com.anrisoftware.sscontrol.types.external.SscontrolScript;
import com.anrisoftware.sscontrol.types.external.SscontrolServiceScript;
import com.anrisoftware.sscontrol.unix.internal.core.ArgumentParser.ArgumentParserFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class CmdRun {

    public interface CmdRunFactory {

        CmdRun create(@Assisted("command") String command,
                @Assisted("parent") SscontrolServiceScript parent,
                @Assisted("threads") Threads threads,
                @Assisted("properties") Properties properties,
                @Assisted("profile") ProfileProperties profile,
                @Assisted("scriptLog") Object scriptLog,
                @Assisted("args") Map<String, Object> args);

    }

    private final Map<String, Object> args;

    private final String command;

    private final Object parent;

    private final Threads threads;

    @Inject
    private CmdLogger log;

    @Inject
    private ScriptExecFactory scriptEx;

    @Inject
    private UnixTemplatesProvider templates;

    @Inject
    private RunCommandsArg runCommandsArg;

    @Inject
    private ArgumentParserFactory argumentParserFactory;

    @Inject
    CmdRun(@Assisted("command") String command,
            @Assisted("parent") SscontrolServiceScript parent,
            @Assisted("threads") Threads threads,
            @Assisted("properties") Properties properties,
            @Assisted("profile") ProfileProperties profile,
            @Assisted("scriptLog") Object scriptLog,
            @Assisted("args") Map<String, Object> args) {
        this.args = new HashMap<String, Object>(args);
        this.args.put("log", scriptLog);
        this.args.put("command", getCommand(command, properties));
        this.args.put("useSsh", profile.getProperty("ssh_command"));
        this.args.put("sshHosts", getSshHosts(parent));
        this.args.put("sshArgs", profile.getProperty("ssh_arguments"));
        this.args.put("sshCommand", properties.getProperty("ssh_command"));
        this.parent = parent;
        this.command = command;
        this.threads = threads;
    }

    private Object getSshHosts(SscontrolServiceScript parent) {
        SscontrolScript script = parent.getScriptsRepository().getScript("ssh");
        // TODO Auto-generated method stub
        return null;
    }

    public ProcessTask call() throws CommandExecException {
        String system = getSystem(args);
        String commandName;
        commandName = format(COMMAND_NAME_FORMAT, system, capitalize(command));
        RunCommands runCommands = runCommandsArg.runCommands(args, parent);
        TemplateResource res = templates.get().getResource(command);
        checkArgs(command, commandName, args);
        return runCommand(commandName, runCommands, res);
    }

    private String getCommand(String command, Properties properties) {
        String commandKey = format("%s_command", command);
        String cmd = properties.getProperty(commandKey);
        notBlank(cmd, IS_BLANK, commandKey);
        return cmd;
    }

    private ProcessTask runCommand(String commandName, RunCommands runCommands,
            TemplateResource res) throws CommandExecException {
        ProcessTask task;
        task = scriptEx.create(args, parent, threads, res, commandName).call();
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

    private static final String COMMAND_NAME_FORMAT = "%s%s";

    private static final String IS_BLANK = "%s blank";

    private static final String COMMAND_KEY = "command";

    private static final String COMMAND_ARGS_FORMAT = "%s_args";

}
