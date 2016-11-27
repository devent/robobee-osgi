package com.anrisoftware.sscontrol.replace.internal;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ReplaceLine {

    public interface ReplaceLineFactory {

        ReplaceLine create(Map<String, Object> args);

    }

    private final Map<String, Object> args;

    @Inject
    ReplaceLine(@Assisted Map<String, Object> args) {
        this.args = new HashMap<String, Object>(args);
    }

    public Map<String, Object> getArgs(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<String, Object>(args);
        putIfNotNull(a, "search");
        putIfNotNull(a, "replace");
        putIfNotNull(a, "flags");
        return a;
    }

    private void putIfNotNull(Map<String, Object> a, String name) {
        Object v = args.get(name);
        if (v != null) {
            a.put(name, v);
        }
    }

}
