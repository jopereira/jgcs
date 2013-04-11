
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

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;

/**
 * This class defines a ControlSession.
 * This Session is used to join and leave a simple group. It is also used to register a ControlListener.
 * An instance of this session must be created by the Protocol interface.
 * @see Protocol
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public interface ControlSession extends Closeable {

	/**
	 * Joins the group. It must block until the join process is finished.
	 *
	 */
	public void join() throws GroupException;
	
	/**
	 * Leaves the group. It must block until the leave process is finished.
	 *
	 */
	public void leave() throws GroupException;
	
	/**
	 * Gets the local address. It might return null if the member is not joined to any group
	 * or the session is closed, but not necessarily. This cannot thus be used to determine
	 * if it is joined to a group.
	 * @return the local address or null if not available
	 */
	public SocketAddress getLocalAddress();
	
	/**
	 * Adds a listener to deliver exceptions related to message reception and membership notifications.
	 * @param exception the exception thrown by the implementation of the interface.
	 */
	public void setExceptionListener(ExceptionListener exception)
		throws ClosedSessionException;

	/**
	 * Get the registered listener.
	 * @return the current listener.
	 */
	public ExceptionListener getExceptionListener();
	
	/**
	 * Close this session. This will close the corresponding data sessions, if any. 
	 */
	@Override
	public void close() throws IOException;
	
	/**
	 * Check if this session has been closed.
	 * @return true if already closed
	 */
	public boolean isClosed();
}
