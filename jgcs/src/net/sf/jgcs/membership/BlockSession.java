
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
package net.sf.jgcs.membership;

import net.sf.jgcs.JGCSException;
import net.sf.jgcs.NotJoinedException;

/**
 * 
 * This class defines a BlockSession.
 * This session should be used by toolkits that implement Group Communication 
 * with flush of messages before a view change.
 * 
 * @assoc 1 Notifies 1 BlockListener
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public interface BlockSession extends MembershipSession {

	/**
	 * This method must be used by the application after it received a block 
	 * notification and flushed all pending messages.
	 * After calling this method, the application cannot send any more messages until it receives
	 * a notification of a membership change.
	 * @throws NotJoinedException if the member is not in a group.
	 * @throws JGCSException if an error ocurs.
	 */
	public void blockOk() throws NotJoinedException, JGCSException;
	
	/**
	 * Verifies if the group is blocked or not.
	 * @return true if the group is blocked, false otherwise.
	 * @throws NotJoinedException if the member is not in a group.
	 */
	public boolean isBlocked()  throws NotJoinedException;
	
	/**
	 * Registers a listener for the block notification.
	 * @param listener the listener to register.
	 * @throws JGCSException if an error ocurs.
	 */
	public void setBlockListener(BlockListener listener) throws JGCSException;
	
}
