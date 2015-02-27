
package net.sf.jgcs.tutorial;

import net.sf.jgcs.BlockListener;
import net.sf.jgcs.BlockSession;
import net.sf.jgcs.ControlSession;
import net.sf.jgcs.DataSession;
import net.sf.jgcs.GroupConfiguration;
import net.sf.jgcs.GroupException;
import net.sf.jgcs.InvalidStateException;
import net.sf.jgcs.MembershipListener;
import net.sf.jgcs.MembershipSession;
import net.sf.jgcs.Message;
import net.sf.jgcs.MessageListener;
import net.sf.jgcs.Protocol;
import net.sf.jgcs.ProtocolFactory;
import net.sf.jgcs.Service;

/*
 * Sample application. This sample uses virtual synchrony and implements all the listeners
 * used to receive messages, exceptions and membership notifications.
 */
public class Application implements MessageListener,
	MembershipListener, BlockListener, Runnable {

	private static final int NUM_MESSAGES=10;
	private ControlSession control;
	private DataSession data;
	private Service service;

	/*
	 * The application gets the group configuration objects in the constructor.
	 */
	public Application(ProtocolFactory pf, GroupConfiguration g, Service service) throws GroupException {
		/*
		 * A protocol can now be created. This object represents an instance of
		 * the toolkit that will be used for group communication.
		 */
		Protocol p = pf.createProtocol();
		/*
		 * Using the configuration object provided by a the configuration
		 * process and the previously created protocol, instances of data and
		 * control sessions can now be created. A data session will be used to
		 * send and receive messages. The control session will be used to join
		 * the group and receive notifications concerning the other elements of
		 * the group.
		 */
		this.control = p.openControlSession(g);
		this.data = p.openDataSession(g);
		/*
		 * Store service for sending messages
		 */
		this.service = service;
		/*
		 * The listeners must be set before the application starts using the
		 * group communication toolkit.
		 */
		data.setMessageListener(this);
		if (control instanceof MembershipSession)
			((MembershipSession) control).setMembershipListener(this);
		if (control instanceof BlockSession)
			((BlockSession) control).setBlockListener(this);
	}
	
	/*
	 * This method will run after the creation of the class. At this point, all
	 * the necessary objects were already retrieved from the lookup service. The
	 * application joins the group, sends some messages and finally leaves the
	 * group.
	 */
	public void run() {
		try {
			control.join();
			for (int i = 0; i < NUM_MESSAGES; i++) {
				Thread.sleep(1000);
				/*
				 * A new message object must be created using the data session.
				 */
				Message message = data.createMessage();
				message.setPayload("hello world!".getBytes());
				/*
				 * The message is sent to the group using the service previously
				 * retrieved from the lookup service.
				 */
				data.multicast(message, service, null);
			}
			Thread.sleep(5000);
			/*
			 * All resources should be freed in the control and data sessions.
			 */
			control.leave();
			data.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * This method represents the message listener. Every time that a message is
	 * sent to the group, it is received in this callback by all elements of the
	 * group. The application can return an object to identify this particular
	 * message in the future, but this feature is not used at the moment. This
	 * feature is discussed in other sample.
	 */
	public Object onMessage(Message msg) {
		System.out.println("Message from "+msg.getSenderAddress()
				+": "+new String(msg.getPayload()));
		return null;
	}
	
	/*
	 * This notification is issued every time that the group membership changes.
	 * It is only used if the membership extentions were implemented and may be
	 * used instead of the previous call backs. The new membership may be
	 * retrieved from the membership session.
	 */
	public void onMembershipChange() {
		try {
			System.out.println("-- NEW MEMBERSHIP: " + 
				((MembershipSession) control).getMembership());
		} catch (InvalidStateException e) {
			e.printStackTrace();
		}			
	}
	
	/*
	 * This call back notifies the application that the group will block and a
	 * new membership will be received. The application must flush any pending
	 * messages at this time and call the blockOk method from the control
	 * session. The membership will not be received if the application do not
	 * call this method.
	 */
	public void onBlock() {
		try {
			((BlockSession) control).blockOk();
		} catch (GroupException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * This call back is used to notify the application that it was removed from
	 * the group.
	 */
	public void onExcluded() {
		System.out.println("-- REMOVED from group.");
	}
}
