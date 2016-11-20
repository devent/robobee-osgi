/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-shell-openssh.
 *
 * sscontrol-osgi-shell-openssh is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-shell-openssh is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-shell-openssh. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.shell.internal.ssh;

import java.net.URL;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Provides the command properties for a Linux system.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class LinuxPropertiesProvider extends AbstractContextPropertiesProvider {

    private static final URL res = LinuxPropertiesProvider.class
            .getResource("/linux_cmd.properties");

    LinuxPropertiesProvider() {
        super(LinuxPropertiesProvider.class, res);
    }

    public String getRemoteTempDir() {
        return get().getProperty("ssh_remote_temp_directory");
    }

    public String getSetupCommands() {
        return get().getProperty("ssh_setup_commands");
    }

    public String getCopyFileCommands() {
        return get().getProperty("ssh_copy_file_commands");
    }

    public String getPushFileCommands() {
        return get().getProperty("ssh_push_file_commands");
    }

}
