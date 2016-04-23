package com.anrisoftware.sscontrol.groovy.script.external

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.profile.external.ProfileProperties
import com.anrisoftware.sscontrol.types.external.SscontrolScript

/**
 * Base of all scripts that are using the Groovy syntax and compiler.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
abstract class LinuxScript extends Script {

    /**
     * The {@link String} name of the script.
     */
    String name

    /**
     * The service {@link ProfileProperties} profile properties.
     */
    ProfileProperties profile

    /**
     * The {@link SscontrolService} of the script.
     */
    SscontrolScript service

    /**
     * Returns the default properties for the service, as in example:
     *
     * <pre>
     * ---
     * &#64;Inject
     * &#64;Named("my-properties")
     * ContextProperties myProperties
     *
     * &#64;Override
     * ContextProperties getDefaultProperties() {
     *     myProperties
     * }
     * ---
     * </pre>
     */
    abstract ContextProperties getDefaultProperties()

    @Override
    String toString() {
        new ToStringBuilder(this).append(getName())
    }
}
