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
package com.anrisoftware.sscontrol.replace.internal;

import com.anrisoftware.sscontrol.replace.external.Replace;
import com.anrisoftware.sscontrol.replace.external.Replace.ReplaceFactory;
import com.anrisoftware.sscontrol.replace.internal.CreateTempFileWorker.CreateTempFileWorkerFactory;
import com.anrisoftware.sscontrol.replace.internal.LoadFileWorker.LoadFileWorkerFactory;
import com.anrisoftware.sscontrol.replace.internal.ParseSedSyntax.ParseSedSyntaxFactory;
import com.anrisoftware.sscontrol.replace.internal.PushFileWorker.PushFileWorkerFactory;
import com.anrisoftware.sscontrol.replace.internal.ReplaceWorker.ReplaceWorkerFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ReplaceModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(Replace.class, ReplaceImpl.class)
                .build(ReplaceFactory.class));
        install(new FactoryModuleBuilder()
                .implement(ReplaceWorker.class, ReplaceWorker.class)
                .build(ReplaceWorkerFactory.class));
        install(new FactoryModuleBuilder()
                .implement(LoadFileWorker.class, LoadFileWorker.class)
                .build(LoadFileWorkerFactory.class));
        install(new FactoryModuleBuilder()
                .implement(PushFileWorker.class, PushFileWorker.class)
                .build(PushFileWorkerFactory.class));
        install(new FactoryModuleBuilder()
                .implement(CreateTempFileWorker.class,
                        CreateTempFileWorker.class)
                .build(CreateTempFileWorkerFactory.class));
        install(new FactoryModuleBuilder()
                .implement(ParseSedSyntax.class, ParseSedSyntax.class)
                .build(ParseSedSyntaxFactory.class));
    }

}
