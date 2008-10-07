package org.hivedb.teamcity.plugin;

import java.io.File;

import jetbrains.buildServer.vcs.VcsRoot;

public class GitConfiguration {
  public static final String CLONE_URL = "clone_url";
  
  File workingDirectory;
  File projectDirectory;
  File command;
  String url;
  String ref;

  public File getWorkingDirectory() {
    return workingDirectory;
  }
  
  public File getProjectDirectory() {
    return projectDirectory;
  }

  public File getCommand() {
    return command;
  }
  
  public String getUrl() {
    return url;
  }
  
  public String getRef() {
    return ref;
  }

  public void setRef(String ref) {
    this.ref = ref;
  }

  public boolean isProjectDirectoryARepository() {
    return new File(getProjectDirectory(), ".git").exists();
  }

  public GitConfiguration(File command, File workingDirectory, File projectDirectory, String url, String ref) {
    super();
    this.command = command;
    this.workingDirectory = workingDirectory;
    this.projectDirectory = projectDirectory;
    this.url = url;
    this.ref = ref;
  }
  
  public static GitConfiguration createAgentConfiguration(VcsRoot root, File project) {
    File working = project.getParentFile();
    String url = root.getProperty(CLONE_URL);
    return new GitConfiguration(inferGitCommand(), working, project, url, "master");
  }
  
  public static GitConfiguration createServerConfiguration(VcsRoot root) {
    File working = new File("/tmp/git-teamcity");
    File project = new File("/tmp/git-teamcity/project");
    String url = root.getProperty(CLONE_URL);
    return new GitConfiguration(inferGitCommand(), working, project, url, "master");
  }

  private static File inferGitCommand() {
    String[] defaults = new String[] {
      "C:\\Program Files\\Git\\Cmd\\git-debug.cmd",
      "C:\\Program Files\\Git\\Bin\\git.exe",
      "/usr/bin/git",
    };
    for (String path: defaults) {
      File file = new File(path);
      if (file.exists()) {
        return file;
      }
    }
    throw new RuntimeException("Unable to infer path to Git executable!");
  }
  
  public String toString() {
    return String.format("%s:%s", this.url, this.ref);
  }
}
