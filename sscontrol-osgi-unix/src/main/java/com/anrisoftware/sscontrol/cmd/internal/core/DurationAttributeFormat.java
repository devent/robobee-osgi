package com.anrisoftware.sscontrol.cmd.internal.core;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.inject.Inject;

import org.joda.time.Duration;

import com.anrisoftware.globalpom.durationsimpleformat.DurationSimpleFormatFactory;
import com.anrisoftware.globalpom.durationsimpleformat.UnitMultiplier;
import com.anrisoftware.resources.templates.external.AttributeRenderer;

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
