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
                TemplateResource scriptRes);

    }

    @Inject
    @Assisted
    private TemplateResource scriptRes;

    @Inject
    private LinuxPropertiesProvider linuxPropertiesProvider;

    @Override
    public ProcessTask call() throws CommandExecException {
        ProcessTask task = null;
        task = copyFiles();
        task = fetchFiles();
        task = cleanFiles();
        return task;
    }

    private ProcessTask cleanFiles() throws CommandExecException {
        String tmp = linuxPropertiesProvider.getRemoteTempDir();
        String cmd = linuxPropertiesProvider.getCleanFileCommands();
        String src = FilenameUtils.getName(getSrc());
        Map<String, Object> a = new HashMap<String, Object>(args);
        a.put(COMMAND_ARG, format(cmd, tmp, src));
        return runCmd(a);
    }

    private ProcessTask fetchFiles() throws CommandExecException {
        String tmp = linuxPropertiesProvider.getRemoteTempDir();
        String src = FilenameUtils.getName(getSrc());
        Map<String, Object> a = new HashMap<String, Object>(args);
        a.put(SRC_ARG, format("%s/%s", tmp, src));
        return runScript(scriptRes, a);
    }

    private ProcessTask copyFiles() throws CommandExecException {
        String tmp = linuxPropertiesProvider.getRemoteTempDir();
        String cmd = linuxPropertiesProvider.getCopyFileCommands();
        String src = getSrc();
        String recursive = isRecursive() ? "-r " : "";
        Map<String, Object> a = new HashMap<String, Object>(args);
        a.put(PRIVILEGED_ARG, true);
        a.put(COMMAND_ARG, format(cmd, recursive, tmp, src));
        return runCmd(a);
    }
}
