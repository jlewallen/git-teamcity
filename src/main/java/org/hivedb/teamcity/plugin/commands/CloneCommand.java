package org.hivedb.teamcity.plugin.commands;

import jetbrains.buildServer.vcs.VcsException;

import org.hivedb.teamcity.plugin.GitConfiguration;

import com.intellij.execution.configurations.GeneralCommandLine;

public class CloneCommand extends GitCommand {

  public CloneCommand(GitConfiguration configuration) {
    super(configuration);
  }
  
  protected boolean useProjectDirectory() {
    return false;
  }
  
  protected Integer getTimeout() {
    return 0;
  }

  public void run(boolean getWc) throws VcsException {
    GeneralCommandLine cli = createCommandLine();
    cli.addParameter("clone");
    if (!getWc) {
      cli.addParameter("-n");
    }
    cli.addParameter(getConfiguration().getUrl());
    cli.addParameter(getConfiguration().getProjectDirectory().getAbsolutePath());
    exec(cli); 
  }

}
