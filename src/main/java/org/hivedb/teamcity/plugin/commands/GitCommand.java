package org.hivedb.teamcity.plugin.commands;

import jetbrains.buildServer.ExecResult;

import com.intellij.execution.configurations.GeneralCommandLine;

import jetbrains.buildServer.vcs.VcsException;

import org.hivedb.teamcity.plugin.GitConfiguration;

public class GitCommand {

  GitConfiguration configuration;

  public GitConfiguration getConfiguration() {
    return configuration;
  }

  public GitCommand(GitConfiguration configuration) {
    super();
    this.configuration = configuration;
  }
  
  protected boolean useProjectDirectory() {
    return true;
  }
  
  protected GeneralCommandLine createCommandLine() {
    GeneralCommandLine cli = new GeneralCommandLine();
    cli.setExePath(this.configuration.getCommand().getAbsolutePath());
    cli.setWorkingDirectory(useProjectDirectory() ? this.configuration.getProjectDirectory() : this.configuration.getWorkingDirectory());
    return cli;
  }
	
  protected ExecResult exec(GeneralCommandLine cli) throws VcsException {
    return CommandUtil.runCommand(cli);
  }
  
}
