<head><title>About</title></head>

## Corosync binding

This module contains a Corosync Closed Process Group (CPG) 
implementation using the native C API through JNI. Please see the 
[Corosync home page](http://www.corosync.org) for further details on the abilities of the CPG 
library.

### Building

You need to have Corosync installed and running. Then, to compile the JNI library, first `mvn compile` the `jgcs-corosync` module. Finally use `make -C csrc/` to compile native code.

### Using

First, [add this package as a runtime dependency](dependency-info.html) to your build system. jGCS configuration objects are found in the [net.sf.jgcs.corosync](apidocs/net/sf/jgcs/corosync/jni/package-summary.html) package.

Due to the large stack used by Corosync, 
you need to increase stack size when invoking the JVM. 
Otherwise, you will get a segmentation fault from the JVM. You
also need to set library path to point to wherever you install
the JNI library. You can do both like this:

```
java -Xss65536k -Djava.library.path=csrc/ ...
```

### Native interface

In addition to the standard jGCS interface, package 
[net.sf.jgcs.corosync.jni](apidocs/net/sf/jgcs/corosync/jni/package-summary.html) provides a minimum Java wrapper of the C API 
that can also be used directly.
