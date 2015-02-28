<head><title>Overview</title></head>

The jGCS library provides a generic interface for [group communication](http://en.wikipedia.org/wiki/Group_communication_system) in Java. This interface can be used by applications that need primitives from simple unreliable IP multicast group communication to [virtual synchrony](http://en.wikipedia.org/wiki/Virtual_synchrony) or [atomic broadcast](http://en.wikipedia.org/wiki/Atomic_broadcast). Its a common interface to several existing toolkits that provide different APIs.

Using the inversion of control pattern, this service provides the separation of configuration and use: Applications use a common API that can be implemented using different toolkit bindings. The tollkit that will be used by an application is defined on configuration time. The generic interface has the following features:

- Support for data and membership;

- Support for peer groups and multicast groups;

- Usable from IP multicast to virtual synchrony. It can also be usable for autonomous membership services;

- High concurrency/low latency/latency hiding: Support for optimistic deliveries and semantic protocols;

- Container-managed concurrency: Ut is up to the application decide the thread policy to receive messages (blocking or concurrent non-blocking).

Source code is available at [GitHub](https://github.com/jopereira/jgcs) and Maven binary packages at [BinTray](https://bintray.com/jop/gc). For application development, see the [Generic API module](jgcs/index.html). For configuring
applications with a specific group communication toolkit, see the corresponding binding module.

The jGCS generic API and bindings are open source. Check each module for the its redistribution license.
