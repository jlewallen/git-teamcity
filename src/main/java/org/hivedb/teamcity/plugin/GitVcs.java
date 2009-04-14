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
import org.hivedb.teamcity.plugin.commands.ShowCommand;
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
    log.warn("Getting current version");
    
    GitConfiguration configuration = GitConfiguration.createServerConfiguration(root);
    if (!configuration.isProjectDirectoryARepository()) {
      new CloneCommand(configuration).run(false);
    }
    else {
      new FetchCommand(configuration).run();
    }
    LogCommand getLog = new LogCommand(configuration);
    Commit head = getLog.head();
    if (head == null) {
      log.warn("No current version!");
      return null;
    }
    log.warn("Version: " + head);
    return head.getVersion().toString();
  }

  public String getVersionDisplayName(String version, VcsRoot root) throws VcsException {
    log.warn("Getting version display name: " + version);
    return version;
  }

  public List<ModificationData> collectBuildChanges(VcsRoot root, String fromVersion, String toVersion, IncludeRule includeRule) throws VcsException {
    log.warn(String.format("Getting build changes from %s to %s", fromVersion, toVersion));
    List<ModificationData> modifications = new ArrayList<ModificationData>();
    VersionNumber from = new VersionNumber(fromVersion);
    VersionNumber to = new VersionNumber(toVersion);
    if (from.equals(to)) {
      return modifications;
    }
    GitConfiguration configuration = GitConfiguration.createServerConfiguration(root);
    LogCommand logCommand = new LogCommand(configuration);
    for (Commit commit : logCommand.between(from, to)) {
      log.info(commit);
      List<VcsChange> vcsChanged = new ArrayList<VcsChange>();
      for (NameAndStatus change : commit.getChanges()) {
        log.info(change);
        vcsChanged.add(createChange(change, fromVersion, toVersion));
      }
      modifications.add(new ModificationData(to.getDate(), vcsChanged, commit.getMessage(), commit.getShortAuthor(), root, to.toString(), to.toString()));
    }
    return modifications;
  }
  
  private VcsChange createChange(@NotNull NameAndStatus change,
    @NotNull String from,
    @NotNull String to) {

    switch (change.getStatus()) {
      case ADDED: // fall through
      case COPIED:
        return new VcsChange(VcsChangeInfo.Type.ADDED,
            change.getStatus().toString(), change.getName(), change.getName(),
          from, to);
      case DELETED:
        return new VcsChange(VcsChangeInfo.Type.REMOVED,
           change.getStatus().toString(), change.getName(), change.getName(),
           from,to);
      default:
        return new VcsChange(VcsChangeInfo.Type.CHANGED,
          change.getStatus().toString(), change.getName(), change.getName(),
          from, to);
      }
  }

  public List<ModificationData> collectBuildChanges(VcsRoot root, String fromVersion, String toVersion, CheckoutRules checkoutRules) throws VcsException {
    return VcsSupportUtil.collectBuildChanges(root, fromVersion, toVersion, checkoutRules, this);
  }

  public void buildPatch(VcsRoot root, String fromVersion, String toVersion, PatchBuilder builder, CheckoutRules checkoutRules) throws IOException, VcsException {
    log.warn(String.format("Patch from %s to %s", fromVersion, toVersion));
  }

  @NotNull
  public byte[] getContent(VcsModification vcsModification, VcsChangeInfo change, VcsChangeInfo.ContentType contentType, VcsRoot root) throws VcsException {
    if (change.getType() == VcsChangeInfo.Type.DIRECTORY_REMOVED ) {
      return new byte[] { };
    }
    else {
      String rev = contentType == VcsChangeInfo.ContentType.BEFORE_CHANGE ? change.getBeforeChangeRevisionNumber() : change.getAfterChangeRevisionNumber();
      return getContent(change.getRelativeFileName(), root, rev);
    }
  }

  @NotNull
  public byte[] getContent(String filePath, VcsRoot root, String version) throws VcsException {
    GitConfiguration configuration =
      GitConfiguration.createServerConfiguration(root);
    ShowCommand showCommand = new ShowCommand(configuration);

    return showCommand.run(new VersionNumber(version), filePath);
  }
  
  public String describeVcsRoot(VcsRoot root) {
    GitConfiguration configuration = GitConfiguration.createServerConfiguration(root);
    return configuration.toString();
  }

  public String testConnection(VcsRoot root) throws VcsException {
    throw new UnsupportedOperationException();
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
