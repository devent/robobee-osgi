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

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;

import com.anrisoftware.sscontrol.model.Task;
import com.anrisoftware.sscontrol.model.TaskService;

@OsgiServiceProvider(classes = {TaskService.class})
// The properties below allow to transparently export the service as a web service using Distributed OSGi
@Properties({
    @Property(name = "service.exported.interfaces", value = "*")
})
@Singleton
@Transactional
public class TaskServiceImpl implements TaskService {
    
    @PersistenceContext(unitName="tasklist")
    EntityManager em;

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Task getTask(Integer id) {
        return em.find(Task.class, id);
    }

    @Override
    public void addTask(Task task) {
        em.persist(task);
        em.flush();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Collection<Task> getTasks() {
        CriteriaQuery<Task> query = em.getCriteriaBuilder().createQuery(Task.class);
        return em.createQuery(query.select(query.from(Task.class))).getResultList();
    }

    @Override
    public void updateTask(Task task) {
        em.merge(task);
    }
    
    @Override
    public void deleteTask(Integer id) {
        em.remove(getTask(id));
    }

}
