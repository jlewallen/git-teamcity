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

  public void run() throws VcsException {
    GeneralCommandLine cli = createCommandLine();
    cli.addParameter("clone");
    cli.addParameter(getConfiguration().getUrl());
    cli.addParameter(getConfiguration().getProjectDirectory().getAbsolutePath());
    exec(cli); 
  }

}
