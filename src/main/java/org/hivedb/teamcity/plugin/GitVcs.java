package org.hivedb.teamcity.plugin;

import jetbrains.buildServer.vcs.*;
import jetbrains.buildServer.vcs.patches.PatchBuilder;
import jetbrains.buildServer.Used;
import jetbrains.buildServer.AgentSideCheckoutAbility;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.InvalidProperty;

import java.util.*;
import java.io.IOException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GitVcs extends VcsSupport implements AgentSideCheckoutAbility, VcsPersonalSupport {
  private static final String GIT_COMMAND = "git_command";
  private static final String WORKING_DIRECTORY = "working_directory";

  public List<ModificationData> collectBuildChanges(VcsRoot root, String fromVersion, String currentVersion, CheckoutRules checkoutRules) throws VcsException {
    return null;
  }

  public void buildPatch(VcsRoot root, String fromVersion, String toVersion, PatchBuilder builder, CheckoutRules checkoutRules) throws IOException, VcsException {
  }

  @NotNull
  public byte[] getContent(VcsModification vcsModification, VcsChangeInfo change, VcsChangeInfo.ContentType contentType, VcsRoot vcsRoot) throws VcsException {
    if(change.getType() == VcsChangeInfo.Type.REMOVED || change.getType() == VcsChangeInfo.Type.DIRECTORY_REMOVED )
      return new byte[]{};
    else {
      String rev = contentType == VcsChangeInfo.ContentType.BEFORE_CHANGE ? change.getBeforeChangeRevisionNumber() : change.getAfterChangeRevisionNumber();
      return getContent(change.getRelativeFileName(), vcsRoot, rev);
    }
  }

  @NotNull
  public byte[] getContent(String filePath, VcsRoot versionedRoot, String version) throws VcsException {
    String groomedVersion = version.split("-")[0].trim();
    return git(versionedRoot).show(groomedVersion, filePath).getBytes();
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
    throw new UnsupportedOperationException();
  }

  public String getCurrentVersion(VcsRoot root) throws VcsException {
    return git(root).log(1).iterator().next().getVersion();
  }

  public String describeVcsRoot(VcsRoot vcsRoot) {
    return String.format("%s: %s", vcsRoot.getProperty(WORKING_DIRECTORY));
  }

  public boolean isTestConnectionSupported() {
    return false;
  }

  public String testConnection(VcsRoot vcsRoot) throws VcsException {
    throw new UnsupportedOperationException("Not implemented, git is a distributed version control system. Your repo is local.");
  }

  @Nullable
  public Map<String, String> getDefaultVcsProperties() {
    Map<String,String> p = new HashMap<String,String>();
    p.put(GIT_COMMAND, "/usr/bin/env git");
    p.put(WORKING_DIRECTORY, "./");
    return p;
  }

  public String getVersionDisplayName(String version, VcsRoot root) throws VcsException {
    return git(root).log(1).iterator().next().toString();
  }

  @NotNull
  public Comparator<String> getVersionComparator() {
    return new GitComparator();
  }

  private boolean nullOrEmpty(Object o) {
    return o == null || "".equals(o);
  }

  private Git git(VcsRoot root) {
    return new Git(root.getProperty(GIT_COMMAND));
  }

  public boolean isAgentSideCheckoutAvailable() {
    return true;
  }

  @Nullable
  public String mapFullPath(VcsRoot vcsRoot, String s) {
    String workingDirectory = vcsRoot.getProperty(WORKING_DIRECTORY);
    if(workingDirectory.endsWith("/") && s.startsWith("/"))
      return workingDirectory + s.substring(1);
    else
      return workingDirectory + s;
  }
}
