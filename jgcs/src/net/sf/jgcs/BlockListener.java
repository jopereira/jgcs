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
 * 
 * This class defines a BlockListener.
 * This listener must be used to receive notifications that a group membership will block.
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public interface BlockListener {

	/**
	 * Block notification. Upon this notification, the application must flush
	 * all pending messages and notify the session with the
	 * {@link net.sf.jgcs.BlockSession#blockOk} method.
	 * The view change will not continue if this does not happen.
	 * After the group is blocked, the members cannot send more messages 
	 * until a new Membership view is received.
	 */
	public void onBlock();
	
}
