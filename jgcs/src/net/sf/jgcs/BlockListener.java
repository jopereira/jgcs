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
 * Application interface for delivering block notifcations.
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
