require "spec"
import org.hivedb.teamcity.plugin.Git;
import java.text.SimpleDateFormat;

describe Git do
  before(:all) do
    @git = Git.new '/usr/local/bin/git'
  end

  it 'should parse commit logs properly' do

  end

  it 'should fetch the requested number of commit logs' do
    @git.log(3).size.should equal(3)
  end
end