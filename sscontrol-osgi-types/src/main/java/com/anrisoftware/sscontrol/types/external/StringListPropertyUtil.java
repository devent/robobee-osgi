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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class StringListPropertyUtil {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ListProperty {

        void add(String property);
    }

    /**
     * Provides the list property statement.
     * 
     * <pre>
     * property << 'name = value'
     * </pre>
     */
    @SuppressWarnings("serial")
    public static List<String> stringListStatement(
            final ListProperty property) {
        return new ArrayList<String>() {

            @Override
            public boolean add(String e) {
                property.add(e);
                return true;
            }

            @Override
            public void add(int index, String element) {
                property.add(element);
            }

            @Override
            public boolean addAll(Collection<? extends String> c) {
                for (String string : c) {
                    add(string);
                }
                return true;
            }

            @Override
            public boolean addAll(int index, Collection<? extends String> c) {
                for (String string : c) {
                    add(string);
                }
                return true;
            }
        };
    }

}
