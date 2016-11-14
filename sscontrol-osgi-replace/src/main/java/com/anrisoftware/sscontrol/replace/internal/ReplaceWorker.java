package com.anrisoftware.sscontrol.replace.internal;

import static com.anrisoftware.sscontrol.replace.external.Replace.BEGIN_TOKEN_ARG;
import static com.anrisoftware.sscontrol.replace.external.Replace.END_TOKEN_ARG;
import static com.anrisoftware.sscontrol.replace.external.Replace.REPLACE_ARG;
import static com.anrisoftware.sscontrol.replace.external.Replace.SEARCH_ARG;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenMarker;
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate;
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokensTemplate;
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokensTemplateFactory;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ReplaceWorker implements Callable<TokensTemplate> {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ReplaceWorkerFactory {

        ReplaceWorker create(@Assisted Map<String, Object> args,
                @Assisted String text);

    }

    private final Map<String, Object> args;

    private final String text;

    @Inject
    private TokensTemplateFactory tokensTemplateFactory;

    @Inject
    ReplaceWorker(@Assisted Map<String, Object> args, @Assisted String text,
            PropertiesProvider propertiesProvider) {
        this.args = new HashMap<String, Object>(args);
        this.text = text;
        setupDefaults(propertiesProvider);
        checkArgs();
    }

    @Override
    public TokensTemplate call() throws AppException {
        String beginToken = args.get(BEGIN_TOKEN_ARG).toString();
        String endToken = args.get(END_TOKEN_ARG).toString();
        TokenMarker tokens = new TokenMarker(beginToken, endToken);
        String search = args.get(SEARCH_ARG).toString();
        String replace = args.get(REPLACE_ARG).toString();
        TokenTemplate template = new TokenTemplate(args, search, replace);
        return tokensTemplateFactory.create(tokens, template, text).replace();
    }

    private void checkArgs() {
        notNull(args.get(REPLACE_ARG), "%s=null", REPLACE_ARG);
        notNull(args.get(SEARCH_ARG), "%s=null", SEARCH_ARG);
    }

    private void setupDefaults(PropertiesProvider propertiesProvider) {
        ContextProperties p = propertiesProvider.getProperties();
        if (!args.containsKey(BEGIN_TOKEN_ARG)) {
            args.put(BEGIN_TOKEN_ARG, p.getProperty("default_begin_token"));
        }
        if (!args.containsKey(END_TOKEN_ARG)) {
            args.put(END_TOKEN_ARG, p.getProperty("default_end_token"));
        }
    }

}
