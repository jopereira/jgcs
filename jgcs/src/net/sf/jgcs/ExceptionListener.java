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
 * This class defines a ExceptionListener.
 * This listener must be used to receive exceptions that could occour on message reception.
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public interface ExceptionListener {

	/**
	 * Notification of an exception that occurred when the underlying implementation
	 * was receiving a message.
	 * @param exception the exception.
	 */
	public void onException(GroupException exception);

}
