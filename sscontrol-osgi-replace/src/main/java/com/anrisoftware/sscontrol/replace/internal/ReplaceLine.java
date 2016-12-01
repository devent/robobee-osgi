package com.anrisoftware.sscontrol.replace.internal;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.replace.internal.ParseSedSyntax.ParseSedSyntaxFactory;
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
    private ParseSedSyntaxFactory sedFactory;

    @Inject
    ReplaceLine(@Assisted Map<String, Object> args) {
        this.args = new HashMap<String, Object>(args);
    }

    public Map<String, Object> getArgs(Map<String, Object> a) {
        a = new HashMap<String, Object>(a);
        if (args.get("search") == null) {
            ParseSedSyntax sed;
            sed = sedFactory.create(args.get("replace").toString());
            sed.parse();
            a.put("search", sed.getSearch());
            a.put("replace", sed.getReplace());
        }
        return a;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(args).toString();
    }

}
