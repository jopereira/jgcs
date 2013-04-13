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
 * Application interface for delivering asynchronous exceptions. 
 */
public interface ExceptionListener {

	/**
	 * Notification of an exception that occurred when the underlying implementation
	 * was receiving or otherwise processing a message.
	 * @param exception the exception.
	 */
	public void onException(GroupException exception);

}
