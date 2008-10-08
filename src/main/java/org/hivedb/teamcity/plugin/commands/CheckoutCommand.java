package org.hivedb.teamcity.plugin.commands;

import jetbrains.buildServer.vcs.VcsException;

import org.hivedb.teamcity.plugin.GitConfiguration;
import com.intellij.execution.configurations.GeneralCommandLine;

public class CheckoutCommand extends GitCommand {

  public CheckoutCommand(GitConfiguration configuration) {
    super(configuration);
  }
  
  public void forceCheckout(String ref) throws VcsException {
    GeneralCommandLine cli = createCommandLine();
    cli.addParameter("checkout");
    cli.addParameter("-f");
    cli.addParameter(ref);
    exec(cli); 
  }

}
