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
package com.anrisoftware.sscontrol.service.impl;

import static java.util.Collections.singletonList;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;

/**
 * Allows to export the service without using any XML.
 * Alternatively a blueprint.xml can be put into src/main/resources to
 * use the CXF namespaces to export the service.
 * As another option CXF DOSGi can be used like in the DS example.
 */
@Singleton
public class TaskServiceRestExporter {
    @Inject
    TaskServiceRest taskServiceRest;
    
    @Inject
    Bus bus;
    
    private Server server;

    @PostConstruct
    public void create() {
        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setAddress("/tasklistRest");
        factory.setServiceBean(taskServiceRest);
        factory.setBus(bus);
        factory.setFeatures(singletonList(new LoggingFeature()));
        server = factory.create();
        server.start();
    }
    
    @PreDestroy
    public void destroy() {
        server.destroy();
    }
}
