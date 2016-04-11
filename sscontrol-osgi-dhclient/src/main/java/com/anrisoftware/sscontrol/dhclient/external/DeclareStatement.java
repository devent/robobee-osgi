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
package com.anrisoftware.sscontrol.dhclient.external;

import java.util.List;

/**
 * Interface, alias and lease statement.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DeclareStatement extends Statement {

    /**
     * Returns the statement name, for example, "interface", "alias" or "name".
     *
     * <pre>
     * alias {
     *      interface "eth0";
     *      fixed-address 192.33.137.200;
     * }
     * </pre>
     */
    String getName();

    /**
     * Returns the statement value or {@code null}, for example, "ep0".
     *
     * <pre>
     * interface "ep0" {
     *      send dhcp-client-identifier "my-client-ep0";
     * }
     * </pre>
     */
    String getValue();

    /**
     * Returns the statements.
     */
    List<Statement> getStatements();
}
