require 'buildr/custom_pom'
require 'buildr/jacoco'

VERSION = '0.2-SNAPSHOT'

Release.next_version = '0.2'
Release.commit_message = lambda { |version| "Bump version number to #{version}" }
Release.tag_name = lambda { |version| "v#{version}" }

define 'tape', :group => 'com.vtence.tape', :version => VERSION do
  compile.options.source = '1.7'
  compile.options.target = '1.7'

  test.with :hamcrest, :h2, :flyway

  package :jar
  package_with_javadoc
  package_with_sources

  package :test_jar

  pom.name = 'Tape'
  pom.description = 'A lightweight Java data mapping library'
  pom.add_mit_license
  pom.add_github_project('testinfected/tape')
  pom.add_developer('testinfected', 'Vincent Tence', 'vtence@gmail.com', ['Developer'])
end
