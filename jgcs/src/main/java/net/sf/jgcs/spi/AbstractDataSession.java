/*
 * JGCS - Group Communication Service.
 * Copyright (C) 2006 Nuno Carvalho, Universidade de Lisboa
 * Copyright (C) 2013 Jose' Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs.spi;

import java.util.concurrent.locks.Lock;

import net.sf.jgcs.ClosedSessionException;
import net.sf.jgcs.DataSession;
import net.sf.jgcs.GroupConfiguration;
import net.sf.jgcs.GroupException;
import net.sf.jgcs.Message;
import net.sf.jgcs.MessageListener;
import net.sf.jgcs.Service;
import net.sf.jgcs.ServiceListener;

/**
 * This class defines a AbstractDataSession. 
 * It partially implements the data session interface by handling the listeners and
 * the group configuration.
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public abstract class AbstractDataSession<
	P extends AbstractProtocol<P,DS,CS,G>,
	DS extends AbstractDataSession<P,DS,CS,G>,
	CS extends AbstractControlSession<P,DS,CS,G>,
	G extends GroupConfiguration>
	implements DataSession {
	protected P protocol;
	protected CS controlSession;
	protected G group;
	protected Lock lock;
	
	private MessageListener msgListener;
	private ServiceListener srvcListener;
	
	protected void boot() {
	}

	protected void cleanup() {
		msgListener = null;
		srvcListener = null;
	}

	@Override
	public void close() throws GroupException {
		protocol.removeSessions(group);
	}
	
	@Override
	public boolean isClosed() {
		return controlSession.isClosed();
	}

	@Override
	public void setMessageListener(MessageListener listener) {
		try {
			lock.lock();
			msgListener = listener;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void setServiceListener(ServiceListener listener) {
		try {
			lock.lock();
			srvcListener = listener;
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public MessageListener getMessageListener() {
		try {
			lock.lock();
			return msgListener;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public ServiceListener getServiceListener() {
		try {
			lock.lock();
			return srvcListener;
		} finally {
			lock.unlock();
		}
	}

	protected void notifyExceptionListeners(GroupException exception) {
		controlSession.notifyExceptionListeners(exception);
	}

	protected Object notifyMessageListeners(Message msg) {
		/* Avoid NPE but invoke callback outside the lock.
		 * This means that there can be callbacks after close(),
		 * but avoids deadlocks.
		 */
		MessageListener listener = null;
		try {
			lock.lock();
			listener = msgListener;		
		} finally {
			lock.unlock();
		}
		if (listener != null)
			return listener.onMessage(msg);
		return null;
	}
	
	protected void notifyServiceListeners(Object context, Service service) {
		/* Avoid NPE but invoke callback outside the lock.
		 * This means that there can be callbacks after close(),
		 * but avoids deadlocks.
		 */
		ServiceListener listener = null;
		try {
			lock.lock();
			listener = srvcListener;		
		} finally {
			lock.unlock();
		}
		if(listener != null)
			listener.onServiceEnsured(context,service);
	}

	protected void notifyListeners(Message msg, Service service) {
		Object cookie = notifyMessageListeners(msg);
		if(cookie !=  null)
			notifyServiceListeners(cookie, service); 		
	}

	@Override
	public G getGroup() {
		try {
			lock.lock();
			return group;
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * Check if the protocol has not been closed.
	 * @throws GroupException
	 */
	protected void onEntry() throws GroupException {
		if (isClosed()) throw new ClosedSessionException();
	}
}
