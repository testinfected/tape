require 'buildr/gpg'
# require 'buildr/jacoco'

VERSION_NUMBER = "1.2.0-SNAPSHOT"

Release.next_version = "1.2.0-SNAPSHOT"
Release.commit_message = lambda { |version| "Bump version number to #{version}" }
Release.tag_name = lambda { |version| "v#{version}" }

define 'tape', :group => 'com.vtence.tape', :version => VERSION_NUMBER do
  compile.options.source = '1.8'
  compile.options.target = '1.8'

  test.with :hamcrest, :h2, :flyway

  package :jar
  package :javadoc
  package :sources

  pom.name = 'Tape'
  pom.description = 'A lightweight Java data mapping library'
  pom.add_mit_license
  pom.add_github_project('testinfected/tape')
  pom.scm_developer_connection = 'scm:hg:git+ssh://git@github.com:testinfected/tape.git'
  pom.add_developer('testinfected', 'Vincent Tence', 'vtence@gmail.com', ['Developer'])
end
