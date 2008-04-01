require "spec"
import org.hivedb.teamcity.plugin.Git;
import org.hivedb.teamcity.plugin.GitVcs;
import org.hivedb.teamcity.plugin.test.FauxVcsRoot;

class MyVcsRoot < FauxVcsRoot
  def getVcsName
    'git'
  end

  def getProperty(name)
    `which git`
  end
end

describe GitVcs do
  before(:each) do
    @git = Git.new '/usr/local/bin/git'
    @vcs = GitVcs.new()
  end
  
  it "should get the latest commit id" do
    last_version = `git log -n 1 | grep commit`
    last_version.gsub!('commit ','').strip!
    version = @vcs.getCurrentVersion(MyVcsRoot.new)
    version.should_not be(nil)
    version.should == last_version
  end

  it 'should get the version display name' do
    logs = [].concat @git.log(1).toArray
    latest = logs.first
    @vcs.getVersionDisplayName(latest.id).should == latest.to_s
  end
end