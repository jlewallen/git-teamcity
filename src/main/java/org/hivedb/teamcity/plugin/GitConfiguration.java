package org.hivedb.teamcity.plugin;

import java.io.File;

import jetbrains.buildServer.vcs.VcsRoot;

public class GitConfiguration {
  public static final String CLONE_URL = "clone_url";
  public static final String BRANCH = "branch";
  public static final String SERVER_PROJECT_DIRECTORY = "server_project_directory";
  
  File workingDirectory;
  File projectDirectory;
  File command;
  String url;
  String branch;

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
  
  public String getBranch() {
    return branch;
  }
  
  public String getRemoteBranch() {
    return "origin/" + getBranch();
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
    this.branch = ref;
  }
  
  public static GitConfiguration createAgentConfiguration(VcsRoot root, File project) {
    File working = project.getParentFile();
    String url = root.getProperty(CLONE_URL);
    String branch = root.getProperty(BRANCH);
    return new GitConfiguration(inferGitCommand(), working, project, url, branch);
  }
  
  public static GitConfiguration createServerConfiguration(VcsRoot root) {
    File project = new File(root.getProperty(SERVER_PROJECT_DIRECTORY));
    File working = project.getParentFile();
    if (!working.exists()) {
      working.mkdirs();
    }
    String url = root.getProperty(CLONE_URL);
    String branch = root.getProperty(BRANCH);
    return new GitConfiguration(inferGitCommand(), working, project, url, branch);
  }

  private static File inferGitCommand() {
    String[] defaults = new String[] {
      "C:\\Program Files\\Git\\Cmd\\git-debug.cmd",
      "C:\\Program Files\\Git\\Cmd\\git.cmd",
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
    return String.format("%s:%s", this.url, this.branch);
  }
}
