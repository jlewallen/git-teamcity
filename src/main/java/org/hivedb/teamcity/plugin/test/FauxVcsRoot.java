package org.hivedb.teamcity.plugin.test;

import jetbrains.buildServer.vcs.VcsRoot;

import java.util.Map;

public class FauxVcsRoot implements VcsRoot {
  public FauxVcsRoot() {}
  public String getVcsName() {
    return "git";
  }

  public String getProperty(String propertyName) {
    return null;
  }

  public String getProperty(String propertyName, String defaultValue) {
    return null;
  }

  public Map<String, String> getProperties() {
    return null;
  }

  public String convertToString() {
    return null;
  }

  public String getName() {
    return null;
  }

  public long getId() {
    return 0;
  }

  public long getRootVersion() {
    return 0;
  }

  public VcsRoot createSecureCopy() {
    return null;
  }
}
