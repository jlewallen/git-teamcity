package org.hivedb.teamcity.plugin.commands;

import jetbrains.buildServer.vcs.VcsException;

import org.hivedb.teamcity.plugin.GitConfiguration;

public class CreateTrackingBranchesCommand extends GitCommand {

  public CreateTrackingBranchesCommand(GitConfiguration configuration) {
    super(configuration);
  }
  
  public void run() throws VcsException {
    BranchCommand branch = new BranchCommand(getConfiguration());
    String[] locals = branch.local();
    for (String name : branch.remote("origin"))
    {
      boolean found = false;
      for (String existing : locals)
      {
        if (existing.equals(name))
        {
          found = true;
          break;
        }
      }
      if (!found) {
        branch.track(name, "origin/" + name);
      }
    }
  }

}
