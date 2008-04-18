package org.hivedb.teamcity.plugin;

import com.intellij.openapi.vcs.AbstractVcs;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GitVcsManager extends AbstractVcs {
  
  public GitVcsManager(Project project) {
    super(project);
  }

  @NonNls
  public String getName() {
    return "Git";
  }

  @NonNls
  public String getDisplayName() {
    return "Git";
  }

  public Configurable getConfigurable() {
    return null;
  }
}
