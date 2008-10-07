package org.hivedb.teamcity.plugin.commands;

import jetbrains.buildServer.vcs.VcsException;

import org.hivedb.teamcity.plugin.GitConfiguration;
import org.hivedb.teamcity.plugin.VersionNumber;

import com.intellij.execution.configurations.GeneralCommandLine;

public class CheckoutCommand extends GitCommand {

  public CheckoutCommand(GitConfiguration configuration) {
    super(configuration);
  }
  
  public void run() throws VcsException {
    GeneralCommandLine cli = createCommandLine();
    cli.addParameter("checkout");
    cli.addParameter(getConfiguration().getRef());
    exec(cli); 
  }
  
  public void run(VersionNumber version) throws VcsException {
    GeneralCommandLine cli = createCommandLine();
    cli.addParameter("checkout");
    cli.addParameter(version.getHash());
    exec(cli); 
  }

}
