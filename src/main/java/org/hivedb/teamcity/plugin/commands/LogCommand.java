package org.hivedb.teamcity.plugin.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

import jetbrains.buildServer.vcs.VcsException;

import org.hivedb.teamcity.plugin.Commit;
import org.hivedb.teamcity.plugin.Constants;
import org.hivedb.teamcity.plugin.GitConfiguration;

import com.intellij.execution.configurations.GeneralCommandLine;

public class LogCommand extends GitCommand {

  public LogCommand(GitConfiguration configuration) {
    super(configuration);
  }
  
  public Collection<Commit> run() throws VcsException {
    GeneralCommandLine cli = createCommandLine();
    cli.addParameter("log");
    return parseCommitLog(exec(cli).getStdout());
  }

  private Collection<Commit> parseCommitLog(String log) {
    Collection<Commit> commits = new ArrayList<Commit>();
    BufferedReader r = new BufferedReader(new StringReader(log));
    String line;
    try {
      Commit current = new Commit();
      while ((line = r.readLine()) != null) {
        String s = line.trim();
        if (s == null || "".equals(s))
          continue;
        else {
          if (s.startsWith("commit")) {
            if (current.isValid()) {
              commits.add(current);
              current = new Commit();
            }
            current.setHash(s.replaceAll("commit", "").trim());
          } else if (s.startsWith("Author"))
            current.setAuthor(s.replaceAll("Author:","").trim());
          else if (s.startsWith("Date")) {
            String dateString = s.replaceAll("Date:","").trim();
            current.setDate(Constants.GIT_DATE.parse(dateString));
          } else {
            current.setMessage(s);
            commits.add(current);
            current = new Commit();
          }
        }
      }
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    catch (ParseException e) {
      throw new RuntimeException(e);
    }
    return commits;
  }

}
