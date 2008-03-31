require "spec"
import org.hivedb.teamcity.plugin.Git;

describe Git do
  before(:all) do
    @git = Git.new '/usr/local/bin/git'
  end


  it "should run the git shell command" do
    @git.run('ls').should_not be(nil)
  end
end