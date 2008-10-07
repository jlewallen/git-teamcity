package org.hivedb.teamcity.plugin;

import java.io.File;
import java.util.*;
import java.io.IOException;

import jetbrains.buildServer.util.*;
import jetbrains.buildServer.vcs.*;
import jetbrains.buildServer.CollectChangesByIncludeRule;
import jetbrains.buildServer.agent.vcs.CheckoutOnAgentVcsSupport;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.BuildProgressLogger;

import org.apache.log4j.Logger;

public class GitVcsOnAgent implements CheckoutOnAgentVcsSupport {
  Logger log = Logger.getLogger(GitVcsOnAgent.class);

  public GitVcsOnAgent(BuildAgentConfiguration agentConf) {
  }

  public void updateSources(BuildProgressLogger logger, File workingDirectory, VcsRoot root, String newVersion, IncludeRule includeRule)
   throws VcsException {
    log.info("updateSources: " + workingDirectory + " " + newVersion);
  }

  public String getName() {
    return "git";
  }
}



