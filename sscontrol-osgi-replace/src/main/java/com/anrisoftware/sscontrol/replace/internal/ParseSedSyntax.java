package com.anrisoftware.sscontrol.replace.internal;

import static java.util.regex.Pattern.quote;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ParseSedSyntax {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ParseSedSyntaxFactory {

        ParseSedSyntax create(@Assisted String string);

    }

    private final String string;

    private String search;

    private String replace;

    private boolean substitute;

    private char separator;

    @Inject
    ParseSedSyntax(@Assisted String string) {
        this.string = string;
        this.substitute = false;
        this.separator = '/';
    }

    public ParseSedSyntax parse() {
        int length = string.length();
        int current = 0;
        if (length > 0) {
            char c = string.charAt(current);
            switch (c) {
            case 's':
                substitute = true;
                current++;
                break;
            }
            separator = string.charAt(current);
        }
        String[] split = split(string, "" + separator, "\\");
        this.search = split[current];
        this.replace = split[current + 1];
        return this;
    }

    public String getSearch() {
        return search;
    }

    public String getReplace() {
        return replace;
    }

    public boolean isSubstitute() {
        return substitute;
    }

    private String[] split(String string, String sep, String escape) {
        String regex = "(?<!" + quote(escape) + ")" + quote(sep);
        return string.split(regex);
    }
}
