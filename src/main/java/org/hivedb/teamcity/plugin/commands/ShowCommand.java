package org.hivedb.teamcity.plugin.commands;

import jetbrains.buildServer.vcs.VcsException;

import org.hivedb.teamcity.plugin.Commit;
import org.hivedb.teamcity.plugin.GitConfiguration;
import org.hivedb.teamcity.plugin.VersionNumber;

public class ShowCommand extends GitCommand {

  public ShowCommand(GitConfiguration configuration) {
    super(configuration);
  }

  public Commit run(VersionNumber version)throws VcsException {
    return new LogCommand(getConfiguration()).run(version);
  }
  
}
