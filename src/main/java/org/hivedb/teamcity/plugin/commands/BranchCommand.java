package org.hivedb.teamcity.plugin.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import jetbrains.buildServer.vcs.VcsException;

import org.hivedb.teamcity.plugin.GitConfiguration;
import com.intellij.execution.configurations.GeneralCommandLine;

public class BranchCommand extends GitCommand {

  public BranchCommand(GitConfiguration configuration) {
    super(configuration);
  }
  
  public String[] local() throws VcsException {
    GeneralCommandLine cli = createCommandLine();
    cli.addParameter("branch");
    cli.addParameter("--no-color");
    return parse(exec(cli).getStdout());
  }

  public String[] remote() throws VcsException {
    GeneralCommandLine cli = createCommandLine();
    cli.addParameter("branch");
    cli.addParameter("--no-color");
    cli.addParameter("-r");
    return parse(exec(cli).getStdout());
  }
  
  public String[] remote(String remote) throws VcsException {
    List<String> branches = new ArrayList<String>();
    for (String branch : remote()) {
      if (branch.startsWith(remote + "/")) {
        branches.add(branch.replaceAll(remote + "/", ""));
      }
    }
    return branches.toArray(new String[0]);
  }
  
  public void delete(String branch) throws VcsException {
    GeneralCommandLine cli = createCommandLine();
    cli.addParameter("branch");
    cli.addParameter("--no-color");
    cli.addParameter("-D");
    cli.addParameter(branch);
    exec(cli);
  }
  
  public void track(String branch, String remote) throws VcsException {
    GeneralCommandLine cli = createCommandLine();
    cli.addParameter("branch");
    cli.addParameter(branch);
    cli.addParameter("--no-color");
    cli.addParameter("--track");
    cli.addParameter(remote);
    exec(cli);
  }

  private String[] parse(String data) {
    List<String> branches = new ArrayList<String>();
    BufferedReader r = new BufferedReader(new StringReader(data));
    try {
      String line;
      while ((line = r.readLine()) != null) {
        line = line.trim();
        if (line == null || line.equals("")) {
          continue;
        }
        else {
          branches.add(line.substring(2));
        }
      }
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    return branches.toArray(new String[0]);
  }
}
