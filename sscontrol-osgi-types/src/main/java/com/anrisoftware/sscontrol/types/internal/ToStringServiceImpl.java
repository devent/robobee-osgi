/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-types.
 *
 * sscontrol-osgi-types is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-types is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-types. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.types.internal;

import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.ArgumentNullException;
import com.anrisoftware.sscontrol.types.external.ToStringService;

public class ToStringServiceImpl implements ToStringService {

    @SuppressWarnings("deprecation")
    @Override
    public String toString(Map<String, Object> args, String arg)
            throws AppException {
        Object value = args.get(arg);
        if (value == null) {
            throw new ArgumentNullException(args, arg);
        }
        return ObjectUtils.toString(value);
    }

}
