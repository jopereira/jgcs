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
 * Application interface for delivering message payloads.
 */
public interface MessageListener {
	
	/**
	 * Delivers a message from to the application. In optimistic protocols,
	 * a context object may be returned to associate the message with
	 * future service notifications.  
	 * 
	 * @param msg The message received from the channel.
	 * @return message context for service notifications.
	 */
	public Object onMessage(Message msg);

}
