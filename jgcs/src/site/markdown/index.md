<head><title>About</title></head>

## Generic API

### Developing an application

To use jGCS in an application:

1. Add [this package as a compile time dependency](dependency-info.html).

2. Provide instances of configuration interfaces that are used to specify the binding: [ProtocolFactory](apidocs/net/sf/jgcs/ProtocolFactory.html), [GroupConfiguration](apidocs/net/sf/jgcs/GroupConfiguration.html), and [Service](apidocs/net/sf/jgcs/Service.html). This can be done, for instance, with dependency injection or with JNDI. If you don't care about protocol independence, hard-code the instances of the desired binding. 

3. Open the communication channel by creating the [Protocol](apidocs/net/sf/jgcs/Protocol.html).

4. Joining and leaving groups is done with the [ControlSession](apidocs/net/sf/jgcs/ControlSession.html) interface.

5. Messages can be exchanged with the [DataSession](apidocs/net/sf/jgcs/DataSession.html) interface. 

An simple application is included in the [tutorial](https://github.com/jopereira/jgcs/tree/master/tutorial). More information is found in the documentation for the [net.sf.jgcs](apidocs/index.html) package.

### Implementing a new binding

First, read the [research paper](http://jgcs.sourceforge.net/_Media/doa-06-13.pdf) and get acquainted with existing bindings.

Implementing a new binding for jGCS should start with the three main configuration interfaces: [ProtocolFactory](apidocs/net/sf/jgcs/ProtocolFactory.html), [GroupConfiguration](apidocs/net/sf/jgcs/GroupConfiguration.html), and [Service](apidocs/net/sf/jgcs/Service.html). You should also consider what features of the protocol are best captured by [Annotation](apidocs/net/sf/jgcs/Annotation.html), instead of [Service](apidocs/net/sf/jgcs/Service.html). 

The main functionality will be contained in the implementation of [Protocol](apidocs/net/sf/jgcs/Protocol.html), [DataSession](apidocs/net/sf/jgcs/DataSession.html), and [ControlSession](apidocs/net/sf/jgcs/ControlSession.html). Abstract classes in [net.sf.jgcs.spi](apidocs/net/sf/jgcs/spi/package-summary.html) package should provide convenient base to extend, depending on the features of the protocol, namely, if it exposes membership information and the threading model used. 

Finally, use the same [tests](testapidocs/index.html) as other bindings to make sure that the new implementation behaves according to the specification. 
