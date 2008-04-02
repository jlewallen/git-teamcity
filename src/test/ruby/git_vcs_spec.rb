require "spec"
import org.hivedb.teamcity.plugin.Git;
import org.hivedb.teamcity.plugin.GitVcs;
import org.hivedb.teamcity.plugin.test.FauxVcsRoot;
import java.text.SimpleDateFormat;
import org.hivedb.teamcity.plugin.Commit;

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
    date = `git log -n 1 | grep Date`
    last_version.gsub!('commit ','').strip!
    date.gsub!('Date: ','').strip!

    git_format = SimpleDateFormat.new(Git::GIT_DATE_FORMAT)
    commit_format = SimpleDateFormat.new(Commit::VERSION_DATE_FORMAT)

    formatted_date = commit_format.format(git_format.parse(date))

    version = @vcs.getCurrentVersion(MyVcsRoot.new)
    version.should_not be(nil)
    version.should == "#{last_version} - #{formatted_date}"
  end

  it 'should get the version display name' do
    logs = [].concat @git.log(1).toArray
    latest = logs.first
    @vcs.getVersionDisplayName(latest.getId, MyVcsRoot.new).should == latest.to_s
  end
end