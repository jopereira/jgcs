
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

import java.net.SocketAddress;

/**
 * 
 * This class defines a AbstractControlSession. It partially implements the ControlSession interface
 * by handling the listeners. It provides methods for notification of listeners
 * that can be used by final implementations.
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public abstract class AbstractControlSession implements ControlSession {
	private ControlListener ctrlListener;
	private ExceptionListener exceptionListener;

	/**
	 * Initializes any variables needed by this session.
	 *
	 */
	protected void boot() {
	}

	/**
	 * Sets the control listener.
	 */
	public synchronized void setControlListener(ControlListener listener) {
		boot();
		ctrlListener = listener;
	}

	/**
	 * Sets the exception listener.
	 */
	public synchronized void setExceptionListener(ExceptionListener listener) {
		boot();
		exceptionListener = listener;		
	}

	/**
	 * Returns true if all listeners are registered.
	 * @return
	 */
	protected synchronized boolean hasAllListeners(){
		return ctrlListener != null;
	}

	/**
	 * Notifies the exception listener.
	 * @param exception the exception to notify.
	 */
	protected void notifyExceptionListeners(JGCSException exception) {
		if(exceptionListener != null)
			exceptionListener.onException(exception);
	}

	/**
	 * Notifies the control listener of a new member in the group.
	 * @param peer the address of the new member.
	 */
	protected synchronized void notifyJoin(SocketAddress peer) {
		if(ctrlListener != null)
			ctrlListener.onJoin(peer);
	}

	/**
	 * Notifies the control listener of a leaved member.
	 * @param peer the address of the old member.
	 */
	protected synchronized void notifyLeave(SocketAddress peer) {
		if(ctrlListener != null)
			ctrlListener.onLeave(peer);
	}

	/**
	 * Notifies the control listener that a member has failed.
	 * @param peer the address of the old member.
	 */	
	protected synchronized void notifyFailed(SocketAddress peer) {
		if(ctrlListener != null)
			ctrlListener.onFailed(peer);
	}

}
