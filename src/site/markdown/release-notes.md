<head><title>Release Notes</title></head>

## Release Notes

### 0.8.1 (2016-04-15)

- set total order as default configuration for JGroups

### 0.8.1 (2015-03-27)

- fix race affecting IP and NeEM bindings
- improve test handling when building

### 0.8.0 (2015-03-02)

- build with Maven
- test with JUnit4
- more documentation
- minor bug fixes

### 0.7.0 (2013-04-22)

- simplification of the API, eliminating redundant functionality (see API documentation and git log for porting instructions)
- removal of support for legacy JGroups 2.x and Spread 3.x
- Java generics are used where appropriate
- refactoring of service provider classes in spi package
- implementation and test of close() method in all protocols and sessions
- major cleanup of synchronization and exception handling
- many minor fixes, for instance, by using FindBugs
- consistent semantics of implementations checked with tests
- much improved JGroups and Corosync bindings

### 0.6.2 (2013-03-28)

- a new Corosync / Closed Process Group (CPG) binding using JNI
- a new ZooKeeper binding, usable only for membership
- minor changes to bindings to use updated versions of protocols
- new build system with Ivy for dependency resolution;
- move to GitHub

### 0.6.1 (2007-10-29)

- legacy version available in [SourceForge](http://jgcs.sf.net)
