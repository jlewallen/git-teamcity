import jetbrains.buildServer.vcs.VcsRoot;
import jetbrains.buildServer.vcs.ModificationData;
import org.hivedb.teamcity.plugin.Commit;
import org.hivedb.teamcity.plugin.Git;
import org.hivedb.teamcity.plugin.GitVcsSupport;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

public class GitVcsSupportTest {
  private Mockery context;
  private String projectName = "git-teamcity";
  private String cloneUrl = "git://github.com/britt/git-teamcity.git";
  private File testDir, projectDir;
  private Git git;
  private GitVcsSupport vcs;
  
  @Before
  public void setUp() {
    context = new JUnit4Mockery() {
      {
        //setImposteriser(ClassImposteriser.INSTANCE);
      }
    };

    testDir = new File(System.getenv("PWD"), "test-checkout");
    projectDir = new File(testDir, projectName);
    if(!testDir.exists())
      testDir.mkdir();
    git = new Git("/usr/local/bin/git", testDir.getAbsolutePath(), projectName);
    vcs = new GitVcsSupport();
    git.clone(cloneUrl);    
  }

  @After
  public void tearDown() throws Exception {
    testDir.delete();
  }

  @Test
  public void shouldGetTheCurrentVersion() throws Exception {
    Commit last = git.log(1).iterator().next();
    String currentVersion = vcs.getCurrentVersion(newVcsRoot());
    Assert.assertEquals(last.getVersion(), currentVersion);
  }

  @Test
  public void shouldRemoveTheDateFromTheDisplayVersion() throws Exception {
    Commit last = git.log(1).iterator().next();
    String currentVersion = vcs.getCurrentVersion(newVcsRoot());
    String displayVersion = vcs.getVersionDisplayName(currentVersion, newVcsRoot());
    Assert.assertEquals(last.getId(), displayVersion);
  }
  
  @Test
  public void shouldGetModificationsOverTheSpecifiedInterval() throws Exception {
    List<Commit> lastThreeCommits = git.log(3);
    List<ModificationData> mods = vcs.collectBuildChanges(newVcsRoot(),lastThreeCommits.get(2).getVersion(),lastThreeCommits.get(0).getVersion(), null);
    Assert.assertEquals(3, mods);
    Assert.assertEquals(mods.get(0).getVersion(), lastThreeCommits.get(0).getVersion());
  }

  @Test
  public void shouldBuildModificationDataProperly() throws Exception {
    List<Commit> commits = git.log(2);
    List<ModificationData> mods = vcs.collectBuildChanges(newVcsRoot(),commits.get(1).getVersion(),commits.get(0).getVersion(), null);
    Assert.assertEquals(commits.get(0).getVersion(), mods.get(0).getVersion());
    Assert.assertEquals(commits.get(0).getDate(), mods.get(0).getVcsDate());
  }

  private VcsRoot newVcsRoot() {
    return new VcsRoot(){
      public String getVcsName() {
        return "git";
      }

      public String getProperty(String s) {
        if(s.equals(GitVcsSupport.GIT_COMMAND))
          return "/usr/local/bin/git";
        else if(s.equals(GitVcsSupport.WORKING_DIRECTORY))
          return testDir.getAbsolutePath();
        else if(s.equals(GitVcsSupport.CLONE_URL))
          return cloneUrl;
        else
          return projectName;
      }

      public String getProperty(String s, String s1) {
        return null;
      }

      public Map<String, String> getProperties() {
        return null;
      }

      public String convertToString() {
        return null;
      }

      public String getName() {
        return null;
      }

      public long getId() {
        return 0;
      }

      public long getRootVersion() {
        return 0;
      }

      public VcsRoot createSecureCopy() {
        return null;
      }
    };
  }
}
