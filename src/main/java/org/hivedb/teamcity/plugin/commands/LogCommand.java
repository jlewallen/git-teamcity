package org.hivedb.teamcity.plugin.commands;

import java.util.Collection;

import jetbrains.buildServer.vcs.VcsException;

import org.hivedb.teamcity.plugin.Commit;
import org.hivedb.teamcity.plugin.GitConfiguration;
import org.hivedb.teamcity.plugin.VersionNumber;

import com.intellij.execution.configurations.GeneralCommandLine;

public class LogCommand extends GitCommand {

  public LogCommand(GitConfiguration configuration) {
    super(configuration);
  }
  
  public Collection<Commit> run() throws VcsException {
    GeneralCommandLine cli = createCommandLine();
    cli.addParameter("log");
    cli.addParameter(getConfiguration().getRef());
    return LogOutput.parse(exec(cli).getStdout());
  }
  
  public Collection<Commit> run(VersionNumber from, VersionNumber to) throws VcsException { 
    GeneralCommandLine cli = createCommandLine();
    cli.addParameter("log");
    cli.addParameter(from.getHash());
    cli.addParameter(to.getHash());
    return LogOutput.parse(exec(cli).getStdout());
  }

  public Commit run(VersionNumber version) throws VcsException { 
    GeneralCommandLine cli = createCommandLine();
    cli.addParameter("log");
    cli.addParameter("-1");
    cli.addParameter(version.getHash());
    Collection<Commit> commits = LogOutput.parse(exec(cli).getStdout()); 
    if (commits.size() != 1) {
      throw new RuntimeException("Unable to log commit: " + version);
    }
    return commits.iterator().next();
  }

}
