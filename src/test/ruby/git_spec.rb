include Java
require 'classpath_loader'
include_class org.hivedb.teamcity.plugin.Git;
include_class java.text.SimpleDateFormat;

describe Git do
  before(:all) do
    @clone_url = 'git://github.com/britt/git-teamcity.git'    
    @test_dir = ENV['PWD'] +  "/test-checkout"
    @project_dir = @test_dir + '/git-teamcity'
    Dir.mkdir @test_dir unless(File.exists?(@test_dir))
    @git = Git.new '/usr/local/bin/git', @test_dir, "git-teamcity"
  end      

  after(:all) do
   IO.popen("rm -rf #{@test_dir}").read
  end

  it "should be able to identify git repositories" do
    Dir.mkdir File.join(@test_dir, ".git") unless(File.exists?(File.join(@test_dir, ".git")))
    @git.isGitRepo(@test_dir).should be(true)
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
    @git.clone(@clone_url)
    @git.isGitRepo(@project_dir).should be(true)
  end

  it 'should be able to do a fetch'

  it 'should be able to do a pull'
  
  it 'should be able to check out a particular branch'

  it 'should be able to create a branch'

  it 'should be able to create a tag'

  it 'should be able to checkout a tag'

  def get_commits(n)
    [].concat @git.log(n).toArray
  end
end