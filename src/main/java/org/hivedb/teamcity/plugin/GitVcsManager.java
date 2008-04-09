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
    return new Configurable(){
      @Nls
      public String getDisplayName() {
        return null;
      }

      @Nullable
      public Icon getIcon() {
        return null;
      }

      @Nullable
      @NonNls
      public String getHelpTopic() {
        return null;
      }

      public JComponent createComponent() {
        return null;
      }

      public boolean isModified() {
        return false;
      }

      public void apply() throws ConfigurationException {
      }

      public void reset() {
      }

      public void disposeUIResources() {
      }
    };
  }
}
