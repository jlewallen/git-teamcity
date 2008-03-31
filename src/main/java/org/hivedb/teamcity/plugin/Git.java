package org.hivedb.teamcity.plugin;


public class Git {
  String gitCommand;

  public Git(String cmd) {
    this.gitCommand = cmd;
  }

  private String runCommand(String cmd) {
    throw new UnsupportedOperationException("Not yet implemented.");
  }

  public String getGitCommand() {
    return gitCommand;
  }  
}
