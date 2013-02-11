
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
 * This class defines a ControlSession.
 * This Session is used to join and leave a simple group. It is also used to register a ControlListener.
 * An instance of this session must be created by the Protocol interface.
 * @see Protocol
 * 
 * @assoc 1 Notifies 1 ControlListener
 * @assoc 1 Notifies 1 ExceptionListener
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public interface ControlSession {

	/**
	 * Joins the group. It must block until the join process is finished.
	 *
	 */
	public void join() throws ClosedSessionException, JGCSException;
	
	/**
	 * Leaves the group. It must block until the leave process is finished.
	 *
	 */
	public void leave() throws ClosedSessionException, JGCSException;
	
	/**
	 * Verifies if the member belongs to a group.
	 * @return true if the member is correctly joined, false otherwise.
	 */
	public boolean isJoined();
	
	/**
	 * Gets the local address. It should return null if the member is not joined to any group.
	 * @return the local address.
	 */
	public SocketAddress getLocalAddress();
	
	/**
	 * Adds a listener to deliver group membership notifications. 
	 * 
	 * @param listener The listener to be bound to the membership service.
	 */
	public void setControlListener(ControlListener listener);

	/**
	 * Adds a listener to deliver exceptions related to message reception and membership notifications.
	 * @param exception the exception thrown by the implementation of the interface.
	 */
	public void setExceptionListener(ExceptionListener exception)
		throws ClosedSessionException;

}
