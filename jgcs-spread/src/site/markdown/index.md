<head><title>About</title></head>

## Spread binding

This module contains a Spread/FlushSpread implementation, that provides
group communication with the Quality of Service defined on
configuration. This means that with this implementation, the
application can use group communication with membership, and several
different properties for message delivery. Please see the [Spread Toolkit home
page](http://www.spread.org) for further details.

### Building

You need to have Spread installed and running. Then, to compile the JNI library, first `mvn compile` the `jgcs-spread` module. Finally use `make -C csrc/` to compile native code.

### Using

First, [add this package as a runtime dependency](dependency-info.html) to your build system. jGCS configuration objects are found in the [net.sf.jgcs.spread](apidocs/net/sf/jgcs/spread/package-summary.html) package.

You need to set library path to point to wherever you install
the JNI library. You can do it like this:

```
java -Djava.library.path=csrc/ ...
```

### Native interface

In addition to the standard jGCS interface, package 
[net.sf.jgcs.spread.jni](apidocs/net/sf/jgcs/spread/jni/package-summary.html) provides a minimum Java wrapper of the C API 
that can also be used directly.
