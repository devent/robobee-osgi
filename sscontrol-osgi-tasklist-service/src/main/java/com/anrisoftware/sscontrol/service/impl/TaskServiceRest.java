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

import java.net.URI;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.ops4j.pax.cdi.api.OsgiService;

import com.anrisoftware.sscontrol.model.Task;
import com.anrisoftware.sscontrol.model.TaskService;

@Singleton
@Consumes({"application/json", "test/xml"})
@Produces({"application/json", "test/xml"})
public class TaskServiceRest {

    public static void main() {
        System.out.println("test");
    }

    @OsgiService @Inject
    TaskService taskService;

    @Context
    UriInfo uri;

    @GET
    @Path("{id}")
    public Response getTask(@PathParam("id") Integer id) {
        Task task = taskService.getTask(id);
        return task == null ? Response.status(Status.NOT_FOUND).build() : Response.ok(task).build();
    }

    @POST
    public Response addTask(Task task) {
        taskService.addTask(task);
        URI taskURI = uri.getRequestUriBuilder().path(TaskServiceRest.class, "getTask").build(task.getId());
        return Response.created(taskURI).build();
    }

    @GET
    public Collection<Task> getTasks() {
        return taskService.getTasks();
    }

    @PUT
    @Path("{id}")
    public void updateTask(@PathParam("id") Integer id, Task task) {
        task.setId(id);
        taskService.updateTask(task);
    }

    @DELETE
    @Path("{id}")
    public void deleteTask(@PathParam("id") Integer id) {
        taskService.deleteTask(id);
    }

}
