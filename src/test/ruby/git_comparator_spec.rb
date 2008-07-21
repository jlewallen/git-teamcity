include Java
require 'classpath_loader'
import org.hivedb.teamcity.plugin.Git;
import org.hivedb.teamcity.plugin.GitComparator;

describe GitComparator do     
  before(:each) do
    @project_name = 'git-teamcity'
    @clone_url = 'git://github.com/britt/git-teamcity.git'
    @test_dir = File.join(ENV['PWD'], "test-checkout")
    @project_dir = File.join(@test_dir, @project_name)
    unless(File.exists?(@test_dir))
      Dir.mkdir @test_dir
    end
    @git = Git.new '/usr/local/bin/git', @test_dir, @project_name
    @git.clone(@clone_url)
    @compare = GitComparator.new
  end

  after(:each) do
    IO.popen("rm -rf #{@test_dir}").read
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