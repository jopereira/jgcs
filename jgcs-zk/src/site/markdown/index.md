<head><title>About</title></head>

## ZooKeeper binding

This binding is a simple group communication protocol built on [ZooKeeper](http://zookeeper.apache.org/), 
implementing the traditional virtually synchronous group communication 
model. This includes view synchrony with sending view delivery and 
totally ordered multicast.

*WARNING*: This binding should not be used as a multicast protocol, 
since it makes a write-intensive use of ZooKeeper. Please use other 
JGCS bindings for that purpose. Using it only for keeping membership 
should be ok.

Otherwise, regard it just as (i) an exercise on how the good old 
virtual synchrony maps to the new abstraction and (maybe) how to 
program with ZooKeeper; (ii) a toolkit whose API is very simple and 
directly matches the theoretical literature, for use in classes and 
labs.

To use, [add this package as a runtime dependency](dependency-info.html) to your build system. jGCS configuration objects are found in the [net.sf.jgcs.zk](apidocs/net/sf/jgcs/zk/package-summary.html) package.
