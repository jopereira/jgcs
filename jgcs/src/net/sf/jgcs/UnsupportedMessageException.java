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
 * This class defines a UnsupportedServiceException.
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public class UnsupportedMessageException extends GroupException {

	private static final long serialVersionUID = 5040608814020335800L;

	/**
	 * 
	 * Creates a new UnsupportedServiceException.
	 */
	public UnsupportedMessageException() {
		super();
	}

	/**
	 * 
	 * Creates a new UnsupportedServiceException.
	 * @param group the error message.
	 */
	public UnsupportedMessageException(Message msg) {
		super("unsupported message: "+msg);
	}

}
