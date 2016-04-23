package com.anrisoftware.sscontrol.hostname.linux.external

import com.anrisoftware.sscontrol.groovy.script.external.LinuxScript

/**
 * Configures the <i>hostname</i> service for GNU/Linux systems.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
abstract class HostnameLinux extends LinuxScript {

    @Override
    def run() {
        distributionSpecificConfiguration()
        deployHostnameConfiguration()
        restartService()
    }

    /**
     * Do some distribution specific configuration.
     */
    abstract void distributionSpecificConfiguration()

    /**
     * Deploys the <i>hostname</i> configuration.
     */
    void deployHostnameConfiguration() {
    }

    /**
     * Restarts the <i>hostname</i> service.
     */
    void restartService() {
    }

    /**
     * Returns the current <i>hostname</i> configuration.
     */
    String getCurrentHostnameConfiguration() {
    }

    /**
     * Returns the <i>hostname</i> configuration.
     */
    List getHostnameConfiguration() {
        []
    }

    /**
     * Returns the <i>hostname</i> configuration file, for
     * example {@code "hostname".} If the file path is not absolute then the
     * file is assumed under the configuration directory.
     *
     * <ul>
     * <li>property key {@code configuration_file}</li>
     * </ul>
     */
    File getHostnameFile() {
        profile.getFileProperty "configuration_file", configurationDir, defaultProperties
    }

    /**
     * Returns the <i>hostname</i> configuration file, for
     * example {@code "/etc".}
     *
     * <ul>
     * <li>property key {@code configuration_file}</li>
     * </ul>
     */
    File getConfigurationFile() {
        profile.getFileProperty "configuration_directory", defaultProperties
    }
}
