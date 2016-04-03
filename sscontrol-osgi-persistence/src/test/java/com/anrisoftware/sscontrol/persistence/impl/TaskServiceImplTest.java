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
package com.anrisoftware.sscontrol.persistence.impl;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Assert;
import org.junit.Test;

import com.anrisoftware.sscontrol.model.Task;
import com.anrisoftware.sscontrol.persistence.impl.TaskServiceImpl;

public class TaskServiceImplTest {

    @Test
    public void testWriteRead() throws Exception {
        TaskServiceImpl taskService = new TaskServiceImpl();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tasklist", System.getProperties());
        EntityManager em = emf.createEntityManager();
        taskService.em = em;

        em.getTransaction().begin();
        Task task = new Task();
        task.setId(1);
        task.setTitle("Test task");
        taskService.addTask(task);
        em.getTransaction().commit();
        Collection<Task> persons = taskService.getTasks();

        Assert.assertEquals(1, persons.size());
        Task task1 = persons.iterator().next();
        Assert.assertEquals(new Integer(1), task1.getId());
        Assert.assertEquals("Test task", task1.getTitle());
    }

}
