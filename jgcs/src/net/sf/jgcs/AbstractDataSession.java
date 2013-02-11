
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
package net.sf.jgcs;

/**
 * This class defines a AbstractDataSession. 
 * It partially implements the data session interface by handling the listeners and
 * the group configuration.
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public abstract class AbstractDataSession implements DataSession {
	private MessageListener msgListener;
	private ExceptionListener excpListener;
	private ServiceListener srvcListener;
	private AbstractProtocol protocol;
	private GroupConfiguration group;

	protected AbstractDataSession(AbstractProtocol protocol, GroupConfiguration group) {
		this.protocol=protocol;
		this.group=group;
	}
	
	protected void boot() {
	}

	public void close() {
		protocol.removeSessions(group);
	}

	public synchronized void setMessageListener(MessageListener listener) {
		boot();
		msgListener = listener;
	}

	public synchronized void setExceptionListener(ExceptionListener listener) {
		boot();
		excpListener = listener;
		
	}

	public synchronized void setServiceListener(ServiceListener listener) {
		boot();
		srvcListener = listener;
	}

	protected synchronized void notifyExceptionListeners(JGCSException exception) {
		if(excpListener != null)
			excpListener.onException(exception);
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
