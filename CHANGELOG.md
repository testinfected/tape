# Change Log
All notable changes to this project will be documented in this file.
This project adheres to [Semantic Versioning](http://semver.org/).

## [0.3.0] - 2016-10-21

### Added
- Support for auto-generated columns in schema declaration. 
Auto-generated columns are selected but not inserted nor updated ([#1]) 
- Support single table inheritance through relaxed generic data types

### Changed
- `DriverManagerDataSource` autocommit is now on by default, to follow `DriverManager` defaults
- `JDBCException` can wrap an `SQLException` without an accompanying message
- Selects use table name to fully qualify column names by default ([#2])

### Fixed
- Eliminate superfluous space character in element manipulation description

## [0.2] - 2015-09-03

### Added
- N/A

### Changed
- Separate `Table` from its `TableSchema` to move schema declaration out of `Record` implementation
- Test jar is not longer published 

### Fixed
- Deletes and updates no longer declare throwing SQLException

## 0.1 - 2013-01-26

Initial public release


[0.3.0]: https://github.com/testinfected/tape/compare/v0.3.0...v0.2
[0.2]: https://github.com/testinfected/tape/compare/v0.2...v0.1

[#1]: https://github.com/testinfected/tape/issues/1
[#2]: https://github.com/testinfected/tape/issues/2