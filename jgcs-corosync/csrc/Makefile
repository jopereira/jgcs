
JDK=$${JAVA_HOME}
CLASSES=../target/classes

LDFLAGS=-lcpg -lpthread
CFLAGS=-Wall -I $(JDK)/include -I $(JDK)/include/linux

all: libcpgjgcs.so

libcpgjgcs.so: net_sf_jgcs_corosync_jni_ClosedProcessGroup.h jnistubs.c
	gcc --shared -fPIC $(CFLAGS) jnistubs.c -o libcpgjgcs.so $(LDFLAGS)

net_sf_jgcs_corosync_jni_ClosedProcessGroup.h: ${CLASSES}/net/sf/jgcs/corosync/jni/ClosedProcessGroup.class
	${JDK}/bin/javah -classpath ${CLASSES} net.sf.jgcs.corosync.jni.ClosedProcessGroup

clean:
	rm -f *.h *.so *~

