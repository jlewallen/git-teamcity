package org.hivedb.teamcity.plugin.commands;

import jetbrains.buildServer.vcs.VcsException;

import org.hivedb.teamcity.plugin.GitConfiguration;
import org.hivedb.teamcity.plugin.VersionNumber;

import com.intellij.execution.configurations.GeneralCommandLine;

public class CheckoutCommand extends GitCommand {

  public CheckoutCommand(GitConfiguration configuration) {
    super(configuration);
  }
  
  public void run(String ref) throws VcsException {
    GeneralCommandLine cli = createCommandLine();
    cli.addParameter("checkout");
    cli.addParameter(ref);
    exec(cli); 
  }
    
  public void run(VersionNumber version) throws VcsException {
    run(version.getHash());
  }

}
