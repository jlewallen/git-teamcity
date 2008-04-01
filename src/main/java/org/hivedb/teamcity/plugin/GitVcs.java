package org.hivedb.teamcity.plugin;

import jetbrains.buildServer.vcs.*;
import jetbrains.buildServer.vcs.patches.PatchBuilder;
import jetbrains.buildServer.Used;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.InvalidProperty;

import java.util.*;
import java.io.IOException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GitVcs extends VcsSupport{
  public List<ModificationData> collectBuildChanges(VcsRoot root, String fromVersion, String currentVersion, CheckoutRules checkoutRules) throws VcsException {
    return null;
  }

  public void buildPatch(VcsRoot root, String fromVersion, String toVersion, PatchBuilder builder, CheckoutRules checkoutRules) throws IOException, VcsException {
  }

  @NotNull
  public byte[] getContent(VcsModification vcsModification, VcsChangeInfo change, VcsChangeInfo.ContentType contentType, VcsRoot vcsRoot) throws VcsException {
    return new byte[0];
  }

  @NotNull
  public byte[] getContent(String filePath, VcsRoot versionedRoot, String version) throws VcsException {
    return new byte[0];
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
    return null;
  }

  public String getCurrentVersion(VcsRoot root) throws VcsException {
    return git(root).log(1).iterator().next().getId();
  }

  public String describeVcsRoot(VcsRoot vcsRoot) {
    return null;
  }

  public boolean isTestConnectionSupported() {
    return false;
  }

  public String testConnection(VcsRoot vcsRoot) throws VcsException {
    return null;
  }

  @Nullable
  public Map<String, String> getDefaultVcsProperties() {
    return null;
  }

  public String getVersionDisplayName(String version, VcsRoot root) throws VcsException {
    return null;
  }

  @NotNull
  public Comparator<String> getVersionComparator() {
    return null;
  }

  private boolean nullOrEmpty(Object o) {
    return o == null || "".equals(o);
  }

  private Git git(VcsRoot root) {
    return new Git(root.getProperty("git_command"));
  }
}
