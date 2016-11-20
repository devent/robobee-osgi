package com.anrisoftware.sscontrol.shell.internal.cmd;

import static com.google.inject.Guice.createInjector;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.shell.external.ssh.SshArgs;
import com.anrisoftware.sscontrol.shell.external.ssh.SshArgs.SshArgsFactory;
import com.anrisoftware.sscontrol.shell.external.ssh.SshArgsService;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Component
@Service(SshArgsService.class)
public class SshArgsServiceImpl implements SshArgsService {

    @Inject
    private SshArgsFactory sshArgsFactory;

    @Override
    public SshArgs create(Map<String, Object> args) {
        return sshArgsFactory.create(args);
    }

    @Activate
    protected void start() {
        createInjector(new CmdModule()).injectMembers(this);
    }

}
