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
package com.anrisoftware.sscontrol.ssh.internal;

import static org.apache.commons.lang3.StringUtils.split;

import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.resources.StringToURI;
import com.anrisoftware.globalpom.strings.ToStringService;
import com.anrisoftware.sscontrol.ssh.external.SshHost;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class SshHostImpl implements SshHost {

    public interface SshHostImplFactory {

        SshHostImpl create();

    }

    @Inject
    private ToStringService toString;

    private String host;

    private String user;

    private Integer port;

    private URI key;

    public void host(String host) {
        String[] userHostPort = split(host, "@");
        if (userHostPort.length == 1) {
            this.host = userHostPort[0];
            return;
        }
        this.user = userHostPort[0];
        String[] hostPort = split(userHostPort[1], ":");
        this.host = hostPort[0];
        if (hostPort.length > 1) {
            this.port = Integer.valueOf(hostPort[1]);
        }
    }

    public void host(Map<String, Object> args) {
        this.user = toString.toString(args, "user");
        this.host = toString.toString(args, "host");
        this.port = (Integer) args.get("port");
        if (args.containsKey("key")) {
            this.key = StringToURI.toURI(toString.toString(args, "key"));
        }
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    @Override
    public URI getKey() {
        return key;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("user", user)
                .append("host", host).append("port", port).append("key", key)
                .toString();
    }

}
