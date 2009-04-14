package org.hivedb.teamcity.plugin.commands;

import com.intellij.execution.configurations.GeneralCommandLine;

import jetbrains.buildServer.vcs.VcsException;

import org.hivedb.teamcity.plugin.GitConfiguration;
import org.hivedb.teamcity.plugin.VersionNumber;

public class ShowCommand extends GitCommand {

  public ShowCommand(GitConfiguration configuration) {
    super(configuration);
  }

  public byte[] run(VersionNumber version, String path) throws VcsException {
    GeneralCommandLine cli = createCommandLine();
    cli.addParameter("show");
    cli.addParameter(version.getHash() + ":" + path);
      
    return exec(cli).getByteOut();
  }
}
