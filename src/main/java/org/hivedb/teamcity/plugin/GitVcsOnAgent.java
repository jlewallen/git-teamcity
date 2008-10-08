package org.hivedb.teamcity.plugin;

import java.io.File;

import jetbrains.buildServer.vcs.*;
import jetbrains.buildServer.agent.vcs.CheckoutOnAgentVcsSupport;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.BuildProgressLogger;

import org.apache.log4j.Logger;
import org.hivedb.teamcity.plugin.commands.CheckoutCommand;
import org.hivedb.teamcity.plugin.commands.CloneCommand;
import org.hivedb.teamcity.plugin.commands.FetchCommand;

public class GitVcsOnAgent implements CheckoutOnAgentVcsSupport {
  Logger log = Logger.getLogger(GitVcsOnAgent.class);

  public GitVcsOnAgent(BuildAgentConfiguration agentConf) {
  }

  public void updateSources(BuildProgressLogger logger, File workingDirectory, VcsRoot root, String version, IncludeRule includeRule) throws VcsException {
    GitConfiguration configuration = GitConfiguration.createAgentConfiguration(root, workingDirectory);
    if (!configuration.isProjectDirectoryARepository()) {
      workingDirectory.delete();
      CloneCommand cmd = new CloneCommand(configuration);
      cmd.run(false);
    }
    else {
      FetchCommand cmd = new FetchCommand(configuration);
      cmd.run();
    }
    new CheckoutCommand(configuration).forceCheckout(configuration.getRemoteBranch());
    // new CleanCommand(configuration).everything();
  }

  public String getName() {
    return "git";
  }
}
