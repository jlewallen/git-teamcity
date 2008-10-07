package org.hivedb.teamcity.plugin;

import com.intellij.openapi.vcs.VcsRoot;
import com.intellij.execution.configurations.GeneralCommandLine;
import jetbrains.buildServer.ExecResult;

import java.io.*;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Arrays;
import java.util.Map;
import java.util.Hashtable;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.apache.log4j.Logger;


public class Git {
  Logger log = Logger.getLogger(Git.class);

  File workingDirectory;
  File projectDirectory;
  String[] gitCommand;
  public static final String GIT_DATE_FORMAT = "EEE MMM dd HH:mm:ss yyyy Z";

  public Git(String workingDirectory, String projectDirectory) {
    this.gitCommand = inferGitCommand();
    this.workingDirectory = new File(workingDirectory);
    this.projectDirectory = new File(projectDirectory);
  }

  public Collection<String> revList(String rev1, String rev2) {
    String log = runCommand(
      getGitCommand(new String[]{"rev-list", String.format("%s...%s", rev1, rev2)}),
      new String[]{},
      projectDirectory
    );
    return Arrays.asList(log.split("\n"));
  }

  public Collection<Commit> log(int n) {
    String log = runCommand(
      getGitCommand(new String[]{"log", "-n", new Integer(n).toString()}),
      new String[]{},
      projectDirectory
    );
    return parseCommitLog(log);
  }

  public String show(String rev, String file) {
    return runCommand(
      getGitCommand(new String[]{"show", String.format("%s:%s", rev, file)}),
      new String[]{},
      projectDirectory
    );
  }

  public boolean isGitRepo(String dir) {
    return new File(new File(dir), ".git").exists();
  }

  public String fetch() {
    return runCommand(
      getGitCommand(new String[]{"fetch", "-v"}),
      new String[]{},
      projectDirectory
    );
  }

  public String clone(String url) {
    return runCommand(
      getGitCommand(new String[]{"clone", url, projectDirectory.getAbsolutePath()}),
      new String[]{},
      workingDirectory
    );
  }

  private String unixRun(String[] argz, String[] environment, File workingDirectory) {
    Process cmdProc = null;
    try {
      cmdProc = Runtime.getRuntime().exec(argz, environment, workingDirectory);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    BufferedReader in = new BufferedReader(new InputStreamReader(cmdProc.getInputStream()));
    StringBuilder output = new StringBuilder();
    String line;
    try {
      while((line = in.readLine()) != null) {
        output.append(line);
        log.info(line.toString());
        output.append("\n");
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    BufferedReader errorStream = new BufferedReader(new InputStreamReader(cmdProc.getErrorStream()));
    StringBuilder error = new StringBuilder();
    try {
      while((line = errorStream.readLine()) != null) {
        error.append(line);
        log.info(line.toString());
        error.append("\n");
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return output.toString();
  }

  private String runCommand(String[] argz, String[] environment, File workingDirectory) {
    /*
    if (argz[0].indexOf("Program") < 0) {
      return unixRun(argz, environment, workingDirectory);
    }
    */
    GeneralCommandLine cli = new GeneralCommandLine();
    cli.setExePath(argz[0]);
    cli.setWorkDirectory(workingDirectory.getAbsolutePath());
    for (int i = 1; i < argz.length; ++i) {
      cli.addParameter(argz[i]);
    }
    log.info("Running " + cli);
    try {
      ExecResult res = CommandUtil.runCommand(cli);
      return res.getStdout();
    } 
    catch (/*Vcs*/Exception e) {
      log.error(e.toString());
      return "";
    }
  }

  public String[] getGitCommand(String[] parameters) {
    String[] argz = new String[parameters.length + gitCommand.length];
    for (int i = 0; i < gitCommand.length; ++i) {
      argz[i] = gitCommand[i];
    }
    for (int i = 0; i < parameters.length; ++i) {
      argz[gitCommand.length + i] = parameters[i];
    }
    return argz;
  }

  private Collection<Commit> parseCommitLog(String log) {
    Collection<Commit> commits = new ArrayList<Commit>();
    BufferedReader r = new BufferedReader(new StringReader(log));
    String line;
    try {
      Commit current = new Commit();
      while((line = r.readLine()) != null) {
        String s = line.trim();
        if (s == null || "".equals(s))
          continue;
        else {
          if (s.startsWith("commit")) {
            if (current.isValid()) {
              commits.add(current);
              current = new Commit();
            }
            current.setId(s.replaceAll("commit", "").trim());
          } else if (s.startsWith("Author"))
            current.setAuthor(s.replaceAll("Author:","").trim());
          else if (s.startsWith("Date")){
            String dateString = s.replaceAll("Date:","").trim();
            SimpleDateFormat format = new SimpleDateFormat(GIT_DATE_FORMAT);
            current.setDate(format.parse(dateString));
          } else {
            current.setMessage(s);
            commits.add(current);
            current = new Commit();
          }
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    return commits;
  }


  private String[] inferGitCommand() {
    String[] defaults = new String[] {
      "C:\\Program Files\\Git\\Cmd\\git-debug.cmd",
      "C:\\Program Files\\Git\\Bin\\git.exe",
      "/usr/bin/git",
    };
    for (String path: defaults) {
      if (new File(path).exists()) {
        return new String[] { path };
      }
    }
    throw new RuntimeException("Unable to infer path to Git executable!");
  }

}
