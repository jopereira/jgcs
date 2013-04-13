/*
 * JGCS - Group Communication Service.
 * Copyright (C) 2006 Nuno Carvalho, Universidade de Lisboa
 * Copyright (C) 2013 Jose' Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;

/**
 * Allows a process to control its participation in a process group.
 * Namely, it is used to join and leave a group, but also to  collect exceptions
 * thrown asynchronously by the protocol.
 */
public interface ControlSession extends Closeable {

	/**
	 * Joins the group. For protocols implementing {@link net.sf.jgcs.MembershipSession}
	 * membership, it does not necessarily wait for join to complete. The application
	 * should instead wait for a membership change notification.
	 */
	public void join() throws GroupException;
	
	/**
	 * Leaves the group.
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
