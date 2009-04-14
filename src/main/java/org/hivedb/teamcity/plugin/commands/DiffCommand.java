package org.hivedb.teamcity.plugin.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

import jetbrains.buildServer.vcs.VcsException;

import org.hivedb.teamcity.plugin.GitConfiguration;
import org.hivedb.teamcity.plugin.NameAndStatus;
import org.hivedb.teamcity.plugin.VersionNumber;

import com.intellij.execution.configurations.GeneralCommandLine;

public class DiffCommand extends GitCommand {

  public DiffCommand(GitConfiguration configuration) {
    super(configuration);
  }

  public Collection<NameAndStatus> run(VersionNumber from, VersionNumber to)throws VcsException { 
    GeneralCommandLine cli = createCommandLine();
    cli.addParameter("diff");
    cli.addParameter("--no-color");
    cli.addParameter("--name-status");
    cli.addParameter(from.getHash());
    cli.addParameter(to.getHash());
    return parseNamesAndStatuses(exec(cli).getStdout());
  }
  
  private Collection<NameAndStatus> parseNamesAndStatuses(String data) {
    Collection<NameAndStatus> changes = new ArrayList<NameAndStatus>();
    BufferedReader r = new BufferedReader(new StringReader(data));
    try {
      String line;
      while ((line = r.readLine()) != null) {
        String s = line.trim();
        if (s == null || "".equals(s))
          continue;
        else {
          String[] fields = line.split(" ");
          String status = fields[0].trim();
          String name = fields[1].trim();
          changes.add(new NameAndStatus(name, status));
        }
      }
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    return changes;
  }
  
}
