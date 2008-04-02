require "spec"
import org.hivedb.teamcity.plugin.Git;
import java.text.SimpleDateFormat;

describe Git do
  before(:all) do
    @git = Git.new '/usr/local/bin/git'
  end

  it 'should parse commit logs properly' do
    logs = get_commits(1)
    commit = logs.first
    commit.getMessage.should_not be(nil)
    commit.getAuthor.should_not be(nil)
    commit.getDate.should_not be(nil)
    commit.getId.should_not be(nil)
  end

  it 'should fetch the requested number of commit logs' do
    @git.log(2).size.should equal(2)
  end

  it 'should list the revisions between two commits' do
    commits = get_commits(3)
    head,tail = commits.first, commits.last
    revs = @git.revList(tail.getId, head.getId)
    revs.size.should == 2
    revs.include?(head.getId).should equal(true)
    revs.include?(tail.getId).should equal(false)
  end

  it 'should get the specified revision of a file' do
    commit = get_commits(3).last
    pom = IO.popen("git show #{commit.getId}:pom.xml").read
    @git.show(commit.getId,'pom.xml').should == pom
  end

  def get_commits(n)
    [].concat @git.log(n).toArray
  end
end