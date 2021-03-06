# Change Log
All notable changes to this project will be documented in this file.
This project adheres to [Semantic Versioning](http://semver.org/).

## [2.0.0] - 2019-07-16

### Added

- Support for insertion and update of immutable entities ([#23]) 
  
## [1.2.1] - 2018-07-09

### Changed

- `Table` now exposes its schema, for situations where we want to extend an existing schema ([#22]) 
  
## [1.2.0] - 2018-06-27

### Changed

- `Record` has been broken up into separate functional interfaces ([#21]):
  * `Hydrator` for hydrating
  * `Dehydrator` for hydrating
  * `KeyHandler` for key handling 

## [1.1.0] - 2018-03-02

### Changed

- Generated key handling gets simpler. A default no-op implementation has been added and result
set is automatically advanced to the first result. ([#19])
- `StatementExecutor` is now a pure function to ease integration with existing code. ([#20])

## [1.0.1] - 2017-09-07

### Fixed

- `Count` now works with forward only result sets ([#17])
- `Count` did not provide support for `StatementExecutor`s. This is now fixed ([#18])

## [1.0.0] - 2017-09-02

### Added

- Method to stream query results instead of listing. 
`Select#stream` complements `Select#list` ([#15])
- `StatementExecutor` as an alternative to using `Connection`s directly. 
All statements now provide overloads that accept a `StatementExecutor` ([#16])

### Changed

- `Select#first` now returns an `Optional<T>` instead of a potentially `null` value ([#8])

## [0.5.0] - 2017-08-30

### Added
- Count statements ([#5])

## [0.4.0] - 2017-08-21

### Added
- Support for SQL TIMESTAMP columns ([#4])
- Support for SQL DATE columns ([#6])
- Support for SQL TIME columns ([#9])
- Support for SQL BOOLEAN columns ([#12])

## [0.3.1] - 2016-11-23

### Changed
- Better error reporting when attempting to insert/update an auto-generated column ([#3])

## [0.3.0] - 2016-10-21

### Added
- Support for auto-generated columns in schema declaration. 
Auto-generated columns are selected but not inserted nor updated ([#1]) 
- Support single table inheritance through relaxed generic data types

### Changed
- `DriverManagerDataSource` autocommit is now on by default, to follow `DriverManager` defaults
- `JDBCException` can wrap an `SQLException` without an accompanying message
- Selects use table name to fully qualify column names by default ([#2])

## [0.2] - 2015-09-03

### Added
- N/A

### Changed
- Separate `Table` from its `TableSchema` to move schema declaration out of `Record` implementation
- Test jar is not longer published 

### Fixed
- Deletes and updates no longer declare throwing `SQLException`

## 0.1 - 2013-01-26

Initial public release


[2.0.0]: https://github.com/testinfected/tape/compare/v2.0.0...v1.2.1
[1.2.1]: https://github.com/testinfected/tape/compare/v1.2.1...v1.2.0
[1.2.0]: https://github.com/testinfected/tape/compare/v1.2.0...v1.1.0
[1.1.0]: https://github.com/testinfected/tape/compare/v1.1.0...v1.0.1
[1.0.1]: https://github.com/testinfected/tape/compare/v1.0.1...v1.0.0
[1.0.0]: https://github.com/testinfected/tape/compare/v1.0.0...v0.5.0
[0.5.0]: https://github.com/testinfected/tape/compare/v0.5.0...v0.4.0
[0.4.0]: https://github.com/testinfected/tape/compare/v0.4.0...v0.3.1
[0.3.1]: https://github.com/testinfected/tape/compare/v0.3.1...v0.3.0
[0.3.0]: https://github.com/testinfected/tape/compare/v0.3.0...v0.2
[0.2]: https://github.com/testinfected/tape/compare/v0.2...v0.1

[#1]: https://github.com/testinfected/tape/issues/1
[#2]: https://github.com/testinfected/tape/issues/2
[#3]: https://github.com/testinfected/tape/issues/3
[#4]: https://github.com/testinfected/tape/issues/4
[#5]: https://github.com/testinfected/tape/issues/5
[#6]: https://github.com/testinfected/tape/issues/6
[#8]: https://github.com/testinfected/tape/issues/8
[#9]: https://github.com/testinfected/tape/issues/9
[#12]: https://github.com/testinfected/tape/issues/12
[#15]: https://github.com/testinfected/tape/issues/15
[#16]: https://github.com/testinfected/tape/issues/16
[#17]: https://github.com/testinfected/tape/issues/17
[#18]: https://github.com/testinfected/tape/issues/18
[#19]: https://github.com/testinfected/tape/issues/19
[#20]: https://github.com/testinfected/tape/issues/20
[#21]: https://github.com/testinfected/tape/issues/21
[#22]: https://github.com/testinfected/tape/issues/22
[#23]: https://github.com/testinfected/tape/issues/23