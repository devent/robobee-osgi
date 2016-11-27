package com.anrisoftware.sscontrol.shell.internal.scp;

import static com.anrisoftware.sscontrol.fetch.external.Fetch.SRC_ARG;
import static com.anrisoftware.sscontrol.shell.external.Cmd.COMMAND_ARG;
import static com.anrisoftware.sscontrol.shell.external.Cmd.PRIVILEGED_ARG;
import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
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
public class CopyPrivilegedFileWorker extends AbstractFileWorker
        implements Callable<ProcessTask> {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface CopyPrivilegedFileWorkerFactory {

        CopyPrivilegedFileWorker create(Map<String, Object> args, Object parent,
                Threads threads, Templates templates,
                TemplateResource scriptRes,
                @Assisted("remoteTempDir") String remoteTempDir,
                @Assisted("copyFileCommands") String copyFileCommands);

    }

    @Inject
    @Assisted("remoteTempDir")
    private String remoteTempDir;

    @Inject
    @Assisted("copyFileCommands")
    private String copyFileCommands;

    @Inject
    @Assisted
    private TemplateResource scriptRes;

    @Override
    public ProcessTask call() throws CommandExecException {
        ProcessTask task = null;
        String tmp = remoteTempDir;
        String src = getSrc();
        Map<String, Object> a = new HashMap<String, Object>(args);
        a.put(PRIVILEGED_ARG, true);
        a.put(COMMAND_ARG, format(copyFileCommands, tmp, src));
        task = runCmd(a);
        if (isFileOnly()) {
            src = FilenameUtils.getName(src);
        }
        args.put(SRC_ARG, format("%s/%s", tmp, src));
        task = runScript(scriptRes, args);
        return task;
    }
}
