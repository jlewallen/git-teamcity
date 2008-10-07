package org.hivedb.teamcity.plugin;

import jetbrains.buildServer.vcs.*;
import jetbrains.buildServer.vcs.patches.PatchBuilder;
import jetbrains.buildServer.Used;
import jetbrains.buildServer.AgentSideCheckoutAbility;
import jetbrains.buildServer.CollectChangesByIncludeRule;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.InvalidProperty;

import java.util.*;
import java.io.IOException;

import org.hivedb.teamcity.plugin.commands.CloneCommand;
import org.hivedb.teamcity.plugin.commands.FetchCommand;
import org.hivedb.teamcity.plugin.commands.LogCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.apache.log4j.Logger;
import jetbrains.buildServer.web.openapi.WebResourcesManager;

public class GitVcs extends VcsSupport implements AgentSideCheckoutAbility, VcsPersonalSupport, CollectChangesByIncludeRule {
  Logger log = Logger.getLogger(GitVcs.class);

  private static final String WORKING_DIRECTORY = "working_directory";
  private static final String CLONE_URL = "clone_url";

  public GitVcs(VcsManager vcsmanager, WebResourcesManager resourcesManager) {
    vcsmanager.registerVcsSupport(this);
    resourcesManager.addPluginResources("git", "git-vcs.jar");
  }

  public List<ModificationData> collectBuildChanges(VcsRoot root, String fromVersion, String currentVersion, CheckoutRules checkoutRules) throws VcsException {
    log.warn(String.format("%s: collecting build changes from %s to %s", root.getVcsName(), fromVersion, currentVersion));
    return VcsSupportUtil.collectBuildChanges(root, fromVersion, currentVersion, checkoutRules, this);
  }

  public void buildPatch(VcsRoot root, String fromVersion, String toVersion, PatchBuilder builder, CheckoutRules checkoutRules) throws IOException, VcsException {
    log.warn(String.format("%s: build patch from '%s' to '%s'", root.getVcsName(), fromVersion, toVersion));
    throw new UnsupportedOperationException("Nuh unh!");
  }

  @NotNull
  public byte[] getContent(VcsModification vcsModification, VcsChangeInfo change, VcsChangeInfo.ContentType contentType, VcsRoot vcsRoot) throws VcsException {
    if (change.getType() == VcsChangeInfo.Type.REMOVED || change.getType() == VcsChangeInfo.Type.DIRECTORY_REMOVED ) {
      return new byte[] { };
    }
    else {
      String rev = contentType == VcsChangeInfo.ContentType.BEFORE_CHANGE ? change.getBeforeChangeRevisionNumber() : change.getAfterChangeRevisionNumber();
      return getContent(change.getRelativeFileName(), vcsRoot, rev);
    }
  }

  @NotNull
  public byte[] getContent(String filePath, VcsRoot versionedRoot, String version) throws VcsException {
    return new byte[] { };
  }

  public String getName() {
    return "git";
  }

  @Used("jsp")
  public String getDisplayName() {
    return "Git";
  }

  public PropertiesProcessor getVcsPropertiesProcessor() {
    return new PropertiesProcessor(){
      public Collection<InvalidProperty> process(Map<String, String> properties) {
        return new ArrayList<InvalidProperty>();
      }
    };
  }

  public String getVcsSettingsJspFilePath() {
    return "git_settings.jsp";
  }

  public String getCurrentVersion(VcsRoot root) throws VcsException {
    log.warn(String.format("%s: Getting current version", root.getVcsName()));
    
    GitConfiguration configuration = GitConfiguration.createServerConfiguration(root);
    if (!configuration.isProjectDirectoryARepository()) {
      CloneCommand cmd = new CloneCommand(configuration);
      cmd.run();
    }
    else {
      FetchCommand cmd = new FetchCommand(configuration);
      cmd.run();
    }
    LogCommand getLog = new LogCommand(configuration);
    Collection<Commit> commitLog = getLog.run();
    if (commitLog.isEmpty()) {
      log.warn("No Current Version");
      return null;
    }
    String currentVersion = commitLog.iterator().next().getVersion().toString();
    log.warn("Current Version: " + currentVersion);
    return currentVersion;
  }

  public String describeVcsRoot(VcsRoot vcsRoot) {
    return String.format("%s", vcsRoot.getProperty(CLONE_URL));
  }

  public boolean isTestConnectionSupported() {
    return false;
  }

  public String testConnection(VcsRoot vcsRoot) throws VcsException {
    throw new UnsupportedOperationException("Not implemented, git is a distributed version control system. Your repo is local.");
  }

  @Nullable
  public Map<String, String> getDefaultVcsProperties() {
    return new HashMap<String,String>();
  }

  public String getVersionDisplayName(String version, VcsRoot root) throws VcsException {
    return version;
  }

  @NotNull
  public Comparator<String> getVersionComparator() {
    return new GitComparator();
  }

  public VcsPersonalSupport getPersonalSupport() {
    return this;
  }

  public boolean isAgentSideCheckoutAvailable() {
    return true;
  }

  @Nullable
  public String mapFullPath(VcsRoot vcsRoot, String path) {
    String workingDirectory = vcsRoot.getProperty(WORKING_DIRECTORY);
    if (workingDirectory.endsWith("/") && path.startsWith("/")) {
      return workingDirectory + path.substring(1);
    }
    else {
      return workingDirectory + path;
    }
  }

  public List<ModificationData> collectBuildChanges(VcsRoot vcsRoot, String s, String s1, IncludeRule includeRule) throws VcsException {
    List<ModificationData> modifications = new ArrayList<ModificationData>();
    log.info("collectBuildChanges: " + s + ", " + s1);
    return modifications;
  }
}
