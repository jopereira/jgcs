package net.sf.jgcs.adapters;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.sf.jgcs.ClosedSessionException;
import net.sf.jgcs.DataSession;
import net.sf.jgcs.ExceptionListener;
import net.sf.jgcs.JGCSException;
import net.sf.jgcs.Message;
import net.sf.jgcs.MessageListener;
import net.sf.jgcs.utils.Mailbox;

/**
 * This adapter implements a receive() method on top of the given DataSession.
 * @author nuno
 *
 */
public class BlockingReceiver {

//	private DataSession session;
	private MyListener myListener;
	private JGCSException exception = null;
	private Object context = null;
	private Mailbox<Message> mailbox = null;

	/**
	 * This constructor registers it self as a listener for message and exception listeners.
	 * @param session the data session to be used.
	 * @throws ClosedSessionException
	 */
	public BlockingReceiver(DataSession session) throws ClosedSessionException {
		super();
		mailbox = new Mailbox<Message>();
		myListener = new MyListener(mailbox);
		session.setMessageListener(myListener);
		session.setExceptionListener(myListener);
	}
	
	/**
	 * Receives a message for the group.
	 * @param context the context to be passed to the group communication.
	 * @return the received message.
	 * @throws JGCSException
	 */
	public Message receive(Object context) throws JGCSException {
		myListener.lock.lock();
		try{
			this.context = context;
		}
		finally{
			myListener.lock.unlock();
		}
		Message msg=mailbox.removeNext();
		if(msg == null && exception != null){
			JGCSException ex = null;
			myListener.lock.lock();
			try{
				ex = exception;
				exception = null;
				this.context = null;
			}
			finally{
				myListener.lock.unlock();
			}
			throw ex;
		}
		return msg;
	}

	/**
	 * Receives a message for the group.
	 * @return the received message.
	 * @throws JGCSException
	 */
	public Message receive() throws JGCSException {
		return receive(null);
	}

	/**
	 * Receives a message for the group. This will block until the time expires. If no
	 * message was received until this time, null will be returned.
	 * @param context the context to be passed to the group communication.
	 * @param timeout the time to block (in milliseconds)
	 * @return the received message or null if no message was received.
	 * @throws JGCSException
	 */
	public Message receive(long timeout, Object context) throws JGCSException {
		myListener.lock.lock();
		try{
			this.context = context;
		}
		finally{
			myListener.lock.unlock();
		}
		Message msg=mailbox.removeNext(timeout);
		if(msg == null && exception != null){
			JGCSException ex = null;
			myListener.lock.lock();
			try{
				ex = exception;
				exception = null;
				this.context = null;
			}
			finally{
				myListener.lock.unlock();
			}
			throw ex;
		}
		return msg;
	}
	
	
	private class MyListener  implements MessageListener, ExceptionListener {

		private Mailbox<Message> mbox;
		private final Lock lock = new ReentrantLock();
		private final Condition condition = lock.newCondition();
		
		public MyListener(Mailbox<Message> m){
			mbox = m;
		}
		
		public Object onMessage(Message msg) {
			lock.lock();
			try{
				Object ctx = context;
				context = null;
				mbox.add(msg);
				condition.signalAll();
				return ctx;
			}
			finally{
				lock.unlock();
			}
		}

		public void onException(JGCSException ex) {
			lock.lock();
			try {
				exception = ex;
				condition.signalAll();
			}
			finally{
				lock.unlock();
			}
		}	
	}
	
}
