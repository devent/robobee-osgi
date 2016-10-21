/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-unix.
 *
 * sscontrol-osgi-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-unix. If not, see <http://www.gnu.org/licenses/>.
 */
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
        if (!args.containsKey(name)) {
            return;
        }
        String o = parseTemplate(args, group, p.getProperty(option));
        options.add(o);
    }

    public void addStringOption(String name, String option) {
        if (!args.containsKey(name)) {
            return;
        }
        String a = args.get(name).toString();
        if (isBlank(a)) {
            return;
        }
        String o = parseTemplate(args, group, p.getProperty(option));
        options.add(o);
    }

    public void addPathOption(String name, String string) {
        if (!args.containsKey(name)) {
            return;
        }
        String path = args.get(name).toString();
        if (StringUtils.isBlank(path)) {
            return;
        }
        path = parseTemplate(args, group, path);
        args.put(name, path);
        String pathoption = parseOption(args, group,
                p.getProperty("ssh_control_path_option"));
        pathoption = parseTemplate(args, group, pathoption);
        options.add(pathoption);
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
