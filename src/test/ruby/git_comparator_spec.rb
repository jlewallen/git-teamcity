require "spec"
import org.hivedb.teamcity.plugin.Git;
import org.hivedb.teamcity.plugin.GitComparator;

describe GitComparator do
  before(:each) do
    @git = Git.new '/usr/local/bin/git'
    @compare = GitComparator.new
  end

  it "should return zero when comparing a commit to itself" do
    last_commit = ([].concat @git.log(1).toArray).first
    @compare.compare(last_commit.getVersion, last_commit.getVersion).should equal(0)
  end

  it 'should sort older commits before newer ones' do
    first,second = [].concat @git.log(2).toArray
    @compare.compare(first.getVersion, second.getVersion).should equal(1)
    @compare.compare(second.getVersion, first.getVersion).should equal(-1)
  end
end