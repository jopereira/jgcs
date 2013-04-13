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

/**
 * Allows flushing the group after a block notification.
 */
public interface BlockSession extends MembershipSession {

	/**
	 * This method must be used by the application after it received a block 
	 * notification and flushed all pending messages.
	 * After calling this method, the application cannot send any more messages until it receives
	 * a notification of a membership change.
	 * @throws InvalidStateException if the member is not in a group.
	 * @throws GroupException if an error ocurs.
	 */
	public void blockOk() throws InvalidStateException, GroupException;
	
	/**
	 * Verifies if the group is blocked or not.
	 * @return true if the group is blocked, false otherwise.
	 * @throws InvalidStateException if the member is not in a group.
	 */
	public boolean isBlocked()  throws InvalidStateException;
	
	/**
	 * Registers a listener for the block notification.
	 * @param listener the listener to register.
	 * @throws GroupException if an error ocurs.
	 */
	public void setBlockListener(BlockListener listener) throws GroupException;
	
	/**
	 * Get the registered listener.
	 * @return the current listener.
	 */
	public BlockListener getBlockListener();
}
