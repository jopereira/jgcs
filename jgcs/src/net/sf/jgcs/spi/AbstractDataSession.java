
/*
 * JGCS - Group Communication Service.
 * Copyright (C) 2006 Nuno Carvalho, Universidade de Lisboa
 *
 * jgcs@lasige.di.fc.ul.pt
 *
 * Departamento de Informatica, Universidade de Lisboa
 * Bloco C6, Faculdade de Ciências, Campo Grande, 1749-016 Lisboa, Portugal.
 *
 * See COPYING for licensing details.
 */
package net.sf.jgcs.spi;

import net.sf.jgcs.DataSession;
import net.sf.jgcs.GroupConfiguration;
import net.sf.jgcs.JGCSException;
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
	
	private MessageListener msgListener;
	private ServiceListener srvcListener;
	
	private boolean closed;

	protected void boot() {
	}
	
	protected synchronized void cleanup() {
		if (closed)
			return;
		closed = true;
	}

	@Override
	public void close() throws JGCSException {
		protocol.removeSessions(group);
	}
	
	public synchronized boolean isClosed() {
		return closed;
	}

	public synchronized void setMessageListener(MessageListener listener) {
		boot();
		msgListener = listener;
	}

	public synchronized void setServiceListener(ServiceListener listener) {
		boot();
		srvcListener = listener;
	}

	protected void notifyExceptionListeners(JGCSException exception) {
		controlSession.notifyExceptionListeners(exception);
	}

	protected synchronized Object notifyMessageListeners(Message msg) {
		if(msgListener != null)
			return msgListener.onMessage(msg);
		else
			return null;
	}
	
	protected synchronized void notifyServiceListeners(Object context, Service service) {
		if(srvcListener != null)
			srvcListener.onServiceEnsured(context,service);
	}

	public GroupConfiguration getGroup() {
		return group;
	}
}
