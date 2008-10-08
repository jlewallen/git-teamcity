package org.hivedb.teamcity.plugin.commands;

import jetbrains.buildServer.vcs.VcsException;

import org.hivedb.teamcity.plugin.GitConfiguration;

import com.intellij.execution.configurations.GeneralCommandLine;

public class CleanCommand extends GitCommand {

  public CleanCommand(GitConfiguration configuration) {
    super(configuration);
  }
  
  public void everything() throws VcsException {
    GeneralCommandLine cli = createCommandLine();
    cli.addParameter("clean");
    cli.addParameter("-f");
    cli.addParameter("-d");
    exec(cli); 
  }
}
