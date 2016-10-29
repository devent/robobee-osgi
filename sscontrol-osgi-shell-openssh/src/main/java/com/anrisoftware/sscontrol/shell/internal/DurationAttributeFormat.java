/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-shell-openssh.
 *
 * sscontrol-osgi-shell-openssh is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-shell-openssh is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-shell-openssh. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.shell.internal;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.inject.Inject;

import org.joda.time.Duration;

import com.anrisoftware.globalpom.durationsimpleformat.DurationSimpleFormatFactory;
import com.anrisoftware.globalpom.durationsimpleformat.UnitMultiplier;
import com.anrisoftware.resources.templates.external.AttributeRenderer;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class DurationAttributeFormat implements AttributeRenderer {

    @Inject
    private DurationSimpleFormatFactory durationSimpleFormatFactory;

    private final NumberFormat format;

    DurationAttributeFormat() {
        this.format = new DecimalFormat("0");
    }

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        Duration duration = (Duration) o;
        if ("seconds".equals(formatString)) {
            return durationSimpleFormatFactory.create(format).format(o,
                    UnitMultiplier.SECONDS);
        } else {
            return Long.toString(duration.getStandardSeconds());
        }
    }

    @Override
    public Class<?> getAttributeType() {
        return Duration.class;
    }

}
