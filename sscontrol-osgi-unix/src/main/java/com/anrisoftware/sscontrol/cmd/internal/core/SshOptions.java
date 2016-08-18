package com.anrisoftware.sscontrol.cmd.internal.core;

import static com.anrisoftware.sscontrol.cmd.external.Cmd.DEBUG_LEVEL;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.repeat;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class SshOptions {

    interface SshOptionsFactory {

        SshOptions create(@Assisted Map<String, Object> args,
                @Assisted List<String> options);

    }

    private final List<String> options;

    private final Map<String, Object> args;

    private final ContextProperties p;

    private final STGroup group;

    @Inject
    SshOptions(@Assisted Map<String, Object> args,
            @Assisted List<String> options,
            PropertiesProvider propertiesProvider,
            DurationAttributeFormat durationAttributeFormat) {
        this.args = args;
        this.options = options;
        this.p = propertiesProvider.get();
        this.group = new STGroup();
        group.registerRenderer(durationAttributeFormat.getAttributeType(),
                durationAttributeFormat);
    }

    public void addDefaultOptions() {
        options.addAll(p.getListProperty("default_ssh_options", ";"));
    }

    public void addDebug() {
        Integer level = (Integer) args.get(DEBUG_LEVEL);
        String debug = "";
        if (level > 0) {
            debug = format("-%s",
                    repeat(p.getProperty("ssh_verbose_option"), level));
        }
        args.put(DEBUG_LEVEL, debug);
    }

    public void addOption(String name, String option) {
        if (args.containsKey(name)) {
            String o = parseTemplate(args, group, p.getProperty(option));
            options.add(o);
        }
    }

    public void addStringOption(String name, String option) {
        if (args.containsKey(name)) {
            String a = args.get(name).toString();
            if (isBlank(a)) {
                return;
            }
            String o = parseTemplate(args, group, p.getProperty(option));
            options.add(o);
        }
    }

    public void addPathOption(String name, String string) {
        if (args.containsKey(name)) {
            String path = args.get(name).toString();
            if (!StringUtils.isBlank(path)) {
                path = parseTemplate(args, group, path);
                args.put(name, path);
                String pathoption = parseOption(args, group,
                        p.getProperty("ssh_control_path_option"));
                pathoption = parseTemplate(args, group, pathoption);
                options.add(pathoption);
            }
        }
    }

    private String parseOption(Map<String, Object> args, STGroup group,
            String property) {
        String option = parseTemplate(args, group, property);
        return parseTemplate(args, group, option);
    }

    private String parseTemplate(Map<String, Object> args, STGroup group,
            String template) {
        ST st = new ST(group, template);
        st.add("args", args);
        return st.render();
    }

}
