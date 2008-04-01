require "spec"
import org.hivedb.teamcity.plugin.Git;
import java.text.SimpleDateFormat;

describe Git do
  before(:all) do
    @git = Git.new '/usr/local/bin/git'
  end

  it 'should parse commit logs properly' do
    logs = [].concat @git.log(1).toArray
    commit = logs.first
    commit.getMessage.should_not be(nil)
    commit.getAuthor.should_not be(nil)
    commit.getDate.should_not be(nil)
    commit.getId.should_not be(nil)
  end

  it 'should fetch the requested number of commit logs' do
    @git.log(2).size.should equal(2)
  end
end