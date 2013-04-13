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

/**
 * A protocol instance is the main entry point for group communication.
 * It allows applications to open {@link net.sf.jgcs.DataSession data}
 * and {@link net.sf.jgcs.ControlSession control} sessions associated
 * with specific groups.
 */
public interface Protocol extends Closeable {
	/**
	 * Obtains a data session for a group. This session must be used to send messages
	 * and to register a listener to receive messages from the other members of the group.
	 * A new session is created if it does not yet exist. Otherwise, the existing
	 * session is returned.
	 * 
	 * @param group the configuration.
	 * @return the data session.
	 * @throws GroupException a protocol specific exception.
	 */
	public DataSession openDataSession(GroupConfiguration group) throws GroupException;
	
	/**
	 * Obtains a control session for a group. This session must be used to join and
	 * leave the group, as well as, to obtain information on current membership.
	 * A new session is created if it does not yet exist. Otherwise, the existing
	 * session is returned.
	 * 
	 * @param group the configuration.
	 * @return the control session.
	 * @throws GroupException a protocol specific exception.
	 */
	public ControlSession openControlSession(GroupConfiguration group) throws GroupException;
	
	/**
	 * Close this protocol. This will close all existing associated sessions and prevent any
	 * other from being opened. It will release all resources used by the protocol.
	 */
	@Override
	public void close() throws IOException;
	
	/**
	 * Check if this protocol has been closed.
	 * @return true if already closed
	 */
	public boolean isClosed();
}
