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

  public GitVcs(VcsManager vcsmanager, WebResourcesManager resourcesManager) {
    vcsmanager.registerVcsSupport(this);
    resourcesManager.addPluginResources("git", "git-vcs.jar");
  }

  public String getCurrentVersion(VcsRoot root) throws VcsException {
    log.warn(String.format("%s: Getting current version", root.getVcsName()));
    
    GitConfiguration configuration = GitConfiguration.createServerConfiguration(root);
    if (!configuration.isProjectDirectoryARepository()) {
      new CloneCommand(configuration).run();
    }
    else {
      new FetchCommand(configuration).run();
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

  public String getVersionDisplayName(String version, VcsRoot root) throws VcsException {
    return version;
  }

  public List<ModificationData> collectBuildChanges(VcsRoot root, String fromVersion, String toVersion, IncludeRule includeRule) throws VcsException {
    log.warn(String.format("%s: collecting build changes from %s to %s", root.getVcsName(), fromVersion, toVersion));
    List<ModificationData> modifications = new ArrayList<ModificationData>();
    return modifications;
  }

  public List<ModificationData> collectBuildChanges(VcsRoot root, String fromVersion, String toVersion, CheckoutRules checkoutRules) throws VcsException {
    return VcsSupportUtil.collectBuildChanges(root, fromVersion, toVersion, checkoutRules, this);
  }

  public void buildPatch(VcsRoot root, String fromVersion, String toVersion, PatchBuilder builder, CheckoutRules checkoutRules) throws IOException, VcsException {
    log.warn(String.format("%s: build patch from '%s' to '%s'", root.getVcsName(), fromVersion, toVersion));
    throw new UnsupportedOperationException();
  }

  @NotNull
  public byte[] getContent(VcsModification vcsModification, VcsChangeInfo change, VcsChangeInfo.ContentType contentType, VcsRoot root) throws VcsException {
    if (change.getType() == VcsChangeInfo.Type.REMOVED || change.getType() == VcsChangeInfo.Type.DIRECTORY_REMOVED ) {
      return new byte[] { };
    }
    else {
      String rev = contentType == VcsChangeInfo.ContentType.BEFORE_CHANGE ? change.getBeforeChangeRevisionNumber() : change.getAfterChangeRevisionNumber();
      return getContent(change.getRelativeFileName(), root, rev);
    }
  }

  @NotNull
  public byte[] getContent(String filePath, VcsRoot root, String version) throws VcsException {
    return new byte[] { };
  }
  
  public String describeVcsRoot(VcsRoot root) {
    GitConfiguration configuration = GitConfiguration.createServerConfiguration(root);
    return configuration.toString();
  }

  public String testConnection(VcsRoot root) throws VcsException {
    throw new UnsupportedOperationException("Not implemented, git is a distributed version control system. Your repo is local.");
  }

  public boolean isTestConnectionSupported() {
    return false;
  }

  @Nullable
  public Map<String, String> getDefaultVcsProperties() {
    return new HashMap<String, String>();
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
  public String mapFullPath(VcsRoot root, String path) {
    return null;
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
}
