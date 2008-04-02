require 'rexml/document'

class ClasspathHelper
  def self.project_root
    ENV['PWD']
  end

  def self.project_name
    project_root.split('/').last.to_s
  end

  def self.find_jars(opts = {})
    eclipse_file = opts.key?(:eclipse) ? opts[:eclipse] : "#{project_root}/.classpath"
    idea_file = opts.key?(:idea) ? opts[:idea] : "#{project_root}/#{project_name}.iml"

    jars = []
    if File.exist?(eclipse_file)
      jars = find_jars_eclipse(eclipse_file)
    elsif File.exist?(idea_file)
      jars = find_jars_idea(idea_file)
    end
    return jars
  end

  def self.find_jars_eclipse(classpath_file)
    xml = REXML::Document.new(File.open(classpath_file))
    jars = []
    repo = ENV['M2_REPO'] || '~/.m2/repository'
    xml.root.elements.each('classpathentry[@kind="var"]') do |el|
      jars << el.attributes['path'].gsub('M2_REPO',repo)
    end
    jars
  end

  def self.find_jars_idea(classpath_file)
    xml = REXML::Document.new File.open(classpath_file)
    jars = []
    xml.root.elements.each('*/orderEntry[@type="module-library"]/library/CLASSES/root') do |el|
      jars << el.attributes['url'].gsub('jar://','').gsub('!/','')
    end
    jars
  end
end

classpath ClasspathHelper.find_jars
add_common_classpath true
