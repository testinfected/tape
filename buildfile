define 'tape', :group => 'com.vtence.tape', :version => '0.1-SNAPSHOT' do
  compile.options.source = '1.6'
  compile.options.target = '1.6'

  test.with :hamcrest, :h2, :flyway
  package :jar

  package_with_javadoc
  package_with_sources
end
