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

import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anrisoftware.sscontrol.model.Task;
import com.anrisoftware.sscontrol.model.TaskService;

@Singleton
public class InitHelper {
    Logger LOG = LoggerFactory.getLogger(InitHelper.class);
    @Inject
    TaskService taskService;

    @PostConstruct
    public void addDemoTasks() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (taskService.getTask(1) == null) {
                        addSampleTask();
                    }
                } catch (Exception e) {
                    LOG.warn(e.getMessage(), e);
                }
            }
        });
 
    }

    private void addSampleTask() {
        Task task = new Task(1, "Just a sample task", "Some more info");
        taskService.addTask(task);
    }

}
