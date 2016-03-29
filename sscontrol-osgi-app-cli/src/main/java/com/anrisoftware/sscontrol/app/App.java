/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-app-cli.
 *
 * sscontrol-osgi-app-cli is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-app-cli is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-app-cli. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.app;

import javax.inject.Inject;

import org.apache.karaf.main.Main;

public class App {

    @Inject
    private AppLogger log;

    public void run(String[] args) throws Exception {
        while (true) {
            boolean restart = false;
            boolean restartJvm = false;
            // karaf.restart.jvm take priority over karaf.restart
            System.setProperty("karaf.restart.jvm", "false");
            System.setProperty("karaf.restart", "false");
            Main main = new Main(args);
            launchFramework(main);
            shutdownFramework(restart, restartJvm, main);
        }
    }

    private void launchFramework(final Main main) throws Exception {
        try {
            main.launch();
        } catch (Throwable ex) {
            System.err.println(ex.getMessage());
            log.errorLaunchFramework(ex);
            main.destroy();
            main.setExitCode(-1);
        }
    }

    private void shutdownFramework(boolean restart, boolean restartJvm,
            final Main main) {
        try {
            main.awaitShutdown();
            boolean stopped = main.destroy();
            restart = Boolean.getBoolean("karaf.restart");
            restartJvm = Boolean.getBoolean("karaf.restart.jvm");
            main.updateInstancePidAfterShutdown();
            if (!stopped) {
                if (restart) {
                    log.restartFramework();
                } else {
                    log.timeoutStopFramework();
                    main.setExitCode(-3);
                }
            }
        } catch (Throwable ex) {
            main.setExitCode(-2);
            log.errorShutdownFramework(ex);
            ex.printStackTrace();
        } finally {
            if (restartJvm) {
                System.exit(10);
            } else if (!restart) {
                System.exit(main.getExitCode());
            } else {
                System.gc();
            }
        }
    }

}
