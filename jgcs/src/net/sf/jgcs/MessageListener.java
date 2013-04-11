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
 * This class defines a MessageListener.
 * This listener must be used to receive messages.
 * 
 * @see DataSession
 * @see Service
 * @see ServiceListener
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public interface MessageListener {
	
	/**
	 * Delivers a message from the channel to the application.
	 * To use this listener together with the Services, a cookie must be returned by the application.
	 * 
	 * @param msg The message received from the channel.
	 * @return the cookie of the message.
	 */
	public Object onMessage(Message msg);

}
