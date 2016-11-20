package com.anrisoftware.sscontrol.shell.internal.scp;

import static com.anrisoftware.sscontrol.copy.external.Copy.DEST_ARG;
import static com.anrisoftware.sscontrol.fetch.external.Fetch.SRC_ARG;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.exec.external.scriptprocess.ScriptExecFactory;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.resources.templates.external.TemplateResource;
import com.anrisoftware.resources.templates.external.Templates;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class AbstractFileWorker {

    @Inject
    private AbstractFileWorkerLogger log;

    @Inject
    private ScriptExecFactory scriptEx;

    @Inject
    @Assisted
    protected Map<String, Object> args;

    @Inject
    @Assisted
    private Object parent;

    @Inject
    @Assisted
    private Threads threads;

    @Inject
    @Assisted
    private Templates templates;

    public boolean isRemoteDest() {
        return (Boolean) args.get("remoteDest");
    }

    public boolean isRemoteSrc() {
        return (Boolean) args.get("remoteSrc");
    }

    public String getDest() {
        return args.get(DEST_ARG).toString();
    }

    public String getSrc() {
        return args.get(SRC_ARG).toString();
    }

    protected ProcessTask runScript(TemplateResource res,
            Map<String, Object> args) throws CommandExecException {
        ProcessTask task;
        task = scriptEx.create(args, parent, threads, res, "scpCmd").call();
        log.commandFinished(parent, task, args);
        return task;
    }

    protected ProcessTask runCmd(Map<String, Object> args)
            throws CommandExecException {
        TemplateResource res = templates.getResource("ssh_wrap_bash");
        ProcessTask task;
        task = scriptEx.create(args, parent, threads, res, "sshCmd").call();
        log.commandFinished(parent, task, args);
        return task;
    }

}
