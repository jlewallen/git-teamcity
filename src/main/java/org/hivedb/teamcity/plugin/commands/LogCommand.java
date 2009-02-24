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
  
  public Collection<Commit> between(VersionNumber from, VersionNumber to) throws VcsException {
    GeneralCommandLine cli = createCommandLine();
    cli.addParameter("log");
    cli.addParameter("--no-color");
    cli.addParameter("--name-status");
    if (from.equals(to)) {
      cli.addParameter("-1");      
      cli.addParameter(from.getHash());
    }
    else {
      cli.addParameter(from.getHash() + ".." + to.getHash());
    }
    return LogOutput.parse(exec(cli).getStdout());
  }

  public Commit run(VersionNumber version) throws VcsException { 
    return getSingle(version.getHash());
  }
  
  public Commit head() throws VcsException {
    return getSingle(getConfiguration().getRemoteBranch());
  }
  
  private Commit getSingle(String ref) throws VcsException {
    GeneralCommandLine cli = createCommandLine();
    cli.addParameter("log");
    cli.addParameter("--no-color");
    cli.addParameter("--name-status");
    cli.addParameter("-1");
    cli.addParameter(ref);
    Collection<Commit> commits = LogOutput.parse(exec(cli).getStdout()); 
    if (commits.size() != 1) {
      throw new RuntimeException("Unable to log commit: " + ref);
    }
    return commits.iterator().next();
  }

}
