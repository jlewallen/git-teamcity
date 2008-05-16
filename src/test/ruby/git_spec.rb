include Java
require 'classpath_loader'
include_class org.hivedb.teamcity.plugin.Git;
include_class java.text.SimpleDateFormat;

describe Git do
  before(:all) do
    @test_dir = ENV['PWD'] +  "/test-checkout"
    Dir.mkdir @test_dir
    @clone_url = 'git://github.com/britt/git-teamcity.git'
    @git = Git.new '/usr/local/bin/git'
  end

  after(:all) do
    IO.popen("rm -rf #{@test_dir}").read
  end

  it "should be able to identify git repositories" do
    @git.isGitRepo('/Users/britt/workspace/java/git-vcs').should be(true)
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

  it 'should clone a git repository' do
    @git.clone('git-teamcity', @clone_url, @test_dir)
    @git.isGitRepo(@test_dir + '/git-teamcity').should be(true)
  end

  def get_commits(n)
    [].concat @git.log(n).toArray
  end
end