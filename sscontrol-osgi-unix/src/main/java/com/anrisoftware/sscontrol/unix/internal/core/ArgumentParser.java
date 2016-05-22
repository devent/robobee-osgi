package com.anrisoftware.sscontrol.unix.internal.core;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.google.inject.assistedinject.Assisted;

public class ArgumentParser {

    public interface ArgumentParserFactory {

        ArgumentParser create(@Assisted("command") String command,
                @Assisted("argPropertiesStr") String argPropertiesStr,
                @Assisted Map<String, Object> args);

    }

    private final String command;

    private final String argPropertiesStr;

    private final Map<String, Object> args;

    @Inject
    ArgumentParser(@Assisted("command") String command,
            @Assisted("argPropertiesStr") String argPropertiesStr,
            @Assisted Map<String, Object> args) {
        this.command = command;
        this.argPropertiesStr = argPropertiesStr;
        this.args = args;
    }

    public void parse() throws CommandExecException {
        Properties p = new Properties();
        loadArgs(p);
        for (Object entry : p.keySet()) {
            String arg = entry.toString();
            Matcher matcher = match(p.getProperty(arg), arg);
            Class<?> type = getType(matcher.group(1), command);
            boolean mandatory = Boolean.valueOf(matcher.group(2));
            String descr = matcher.group(3);
            Object foundarg = args.get(arg);
            if (mandatory && foundarg == null) {
                throw new CommandArgumentMandatoryException(command, arg,
                        descr);
            }
            if (!type.isAssignableFrom(String.class)) {
                if (foundarg != null
                        && !type.isAssignableFrom(foundarg.getClass())) {
                    throw new CommandArgumentTypeException(command, arg, type,
                            descr);
                }
            }
        }
    }

    private Matcher match(String string, String arg)
            throws CommandArgumentDescriptionException {
        Matcher matcher = COMMAND_ARG_DESCR.matcher(string);
        if (!matcher.matches()) {
            throw new CommandArgumentDescriptionException(command);
        }
        return matcher;
    }

    private void loadArgs(Properties p)
            throws CommandArgumentDescriptionException {
        try {
            p.load(new StringReader(argPropertiesStr));
        } catch (IOException e) {
            throw new CommandArgumentDescriptionException(e, command);
        }
    }

    private Class<?> getType(String name, String command)
            throws CommandArgumentTypeNotFound {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new CommandArgumentTypeNotFound(e, command);
        }
    }

    private static final Pattern COMMAND_ARG_DESCR = Pattern
            .compile("([\\.\\w]+):(true|false):(.*)");

}
