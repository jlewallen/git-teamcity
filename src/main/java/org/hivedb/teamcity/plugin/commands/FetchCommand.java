package org.hivedb.teamcity.plugin.commands;

import jetbrains.buildServer.vcs.VcsException;

import org.hivedb.teamcity.plugin.GitConfiguration;

import com.intellij.execution.configurations.GeneralCommandLine;

public class FetchCommand extends GitCommand {

  public FetchCommand(GitConfiguration configuration) {
    super(configuration);
  }

  public void run() throws VcsException {
    GeneralCommandLine cli = createCommandLine();
    cli.addParameter("fetch");
    exec(cli); 
  }

}
