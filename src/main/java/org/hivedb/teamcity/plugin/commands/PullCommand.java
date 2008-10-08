package org.hivedb.teamcity.plugin.commands;

import jetbrains.buildServer.vcs.VcsException;

import org.hivedb.teamcity.plugin.GitConfiguration;

import com.intellij.execution.configurations.GeneralCommandLine;

public class PullCommand extends GitCommand {

  public PullCommand(GitConfiguration configuration) {
    super(configuration);
  }

  public void run() throws VcsException {
    GeneralCommandLine cli = createCommandLine();
    cli.addParameter("pull");
    exec(cli); 
  }

}
