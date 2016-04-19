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
package com.anrisoftware.sscontrol.profile.internal;

import static com.anrisoftware.sscontrol.profile.internal.ProfileImplLogger._.profileEntryAdded;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.profile.external.ProfileProperties;

/**
 * Logging for {@link ProfileImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class ProfileImplLogger extends AbstractLogger {

    enum _ {

        profileEntryAdded("Profile entry added {} to {}");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link ProfileImpl}.
     */
    public ProfileImplLogger() {
        super(ProfileImpl.class);
    }

    void profileEntryAdded(ProfileImpl profile, ProfileProperties properties) {
        debug(profileEntryAdded, properties, profile);
    }
}
