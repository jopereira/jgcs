
JDK=$${JAVA_HOME}
CLASSES=../target/classes

LDFLAGS=-lspread
CFLAGS=-Wall -I $(JDK)/include -I $(JDK)/include/linux

all: libspjgcs.so

libspjgcs.so: net_sf_jgcs_spread_jni_Mailbox.h\
		net_sf_jgcs_spread_jni_SpMailbox.h\
		net_sf_jgcs_spread_jni_FlMailbox.h\
		jnistubs.c
	gcc --shared -fPIC $(CFLAGS) jnistubs.c -o libspjgcs.so $(LDFLAGS)

net_sf_jgcs_spread_jni_Mailbox.h: ${CLASSES}/net/sf/jgcs/spread/jni/Mailbox.class
	${JDK}/bin/javah -classpath ${CLASSES} net.sf.jgcs.spread.jni.Mailbox

net_sf_jgcs_spread_jni_SpMailbox.h: ${CLASSES}/net/sf/jgcs/spread/jni/SpMailbox.class
	${JDK}/bin/javah -classpath ${CLASSES} net.sf.jgcs.spread.jni.SpMailbox

net_sf_jgcs_spread_jni_FlMailbox.h: ${CLASSES}/net/sf/jgcs/spread/jni/FlMailbox.class
	${JDK}/bin/javah -classpath ${CLASSES} net.sf.jgcs.spread.jni.FlMailbox

clean:
	rm -f *.h *.so *~

