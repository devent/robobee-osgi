/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.types.external;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Binding host and port.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface BindingHost {

    /**
     * Host bean.
     *
     * @author Erwin Müller, erwin.mueller@deventm.de
     * @since 1.0
     */
    public static final class Host {

        public final String host;

        public final Integer port;

        public Host(String host, Integer port) {
            this.host = host;
            this.port = port;
        }

        @Override
        public String toString() {
            ToStringBuilder b = new ToStringBuilder(this).append(host);
            if (port != null) {
                b.append(port);
            }
            return b.toString();
        }
    }

    /**
     * Returns the binding addresses to add.
     */
    List<Host> getAddedHosts();

    /**
     * Returns the binding addresses to removed.
     */
    List<Host> getRemovedHosts();
}
