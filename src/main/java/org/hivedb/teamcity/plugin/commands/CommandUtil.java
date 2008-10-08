package org.hivedb.teamcity.plugin.commands;

import com.intellij.execution.configurations.GeneralCommandLine;
import jetbrains.buildServer.ExecResult;
import jetbrains.buildServer.SimpleCommandLineProcessRunner;
import jetbrains.buildServer.SimpleCommandLineProcessRunner.RunCommandEvents;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.util.StringUtil;
import jetbrains.buildServer.vcs.VcsException;
import org.jetbrains.annotations.NotNull;

public class CommandUtil {
  public static void checkCommandFailed(@NotNull String cmdName, @NotNull ExecResult res) throws VcsException {
    if (res.getExitCode() > 0 || res.getException() != null) {
      commandFailed(cmdName, res);
    }
    if (res.getStderr().length() > 0) {
      Loggers.VCS.warn("Error output produced by: " + cmdName);
      Loggers.VCS.warn(res.getStderr());
    }
  }

  public static void commandFailed(final String cmdName, final ExecResult res) throws VcsException {
    Throwable exception = res.getException();
    String stderr = res.getStderr();
    String stdout = res.getStdout();
    final String message = "'" + cmdName + "' command failed.\n" +
     (!StringUtil.isEmpty(stderr) ? "stderr: " + stderr + "\n" : "") +
     (!StringUtil.isEmpty(stdout) ? "stdout: " + stdout + "\n" : "") +
     (exception != null ? "exception: " + exception.getLocalizedMessage() : "");
    Loggers.VCS.warn(message);
    throw new VcsException(message);
  }

  public static ExecResult runCommand(@NotNull GeneralCommandLine cli, Integer timeout) throws VcsException {
    String cmdStr = cli.getCommandLineString();
    Loggers.VCS.info("Run Command: " + cmdStr);
    ExecResult res = SimpleCommandLineProcessRunner.runCommand(cli, null, new OurRunCommandEvents(timeout));
    CommandUtil.checkCommandFailed(cmdStr, res);
    Loggers.VCS.info(res.getStdout());
    return res;
  }
  
  public static class OurRunCommandEvents implements RunCommandEvents {
    Integer timeout;
    
    public OurRunCommandEvents(Integer timeout) {
      super();
      this.timeout = timeout;
    }

    public Integer getOutputIdleSecondsTimeout() {
      return Integer.valueOf(this.timeout);
    }

    public void onProcessFinished(Process arg0) {
    }

    public void onProcessStarted(Process arg0) {
    }
  }
}
