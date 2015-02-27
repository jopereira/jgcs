
package net.sf.jgcs.tutorial;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.sf.jgcs.GroupConfiguration;
import net.sf.jgcs.GroupException;
import net.sf.jgcs.ProtocolFactory;
import net.sf.jgcs.Service;

/*
 * Test application using JNDI. Requires configuration of three objects
 * with names myProto, myGroup and myService.
 */
public class JNDITest extends Application {

	/*
	 * Get the jGCS configuration objects from somewhere, in this case, from
	 * JNDI. This means that we have no hard coded dependencies on any group
	 * communication toolkit.
	 */
	public JNDITest(Context ctx) throws GroupException, NamingException {
		super((ProtocolFactory) ctx.lookup("myProto"),
				(GroupConfiguration) ctx.lookup("myGroup"),
				(Service)ctx.lookup("myService"));
	}

	public static void main(String[] args) {
		try {
			Context ctx = new InitialContext();
			Runnable test = new JNDITest(ctx);
			test.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}