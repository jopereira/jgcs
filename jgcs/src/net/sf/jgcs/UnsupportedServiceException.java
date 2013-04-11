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
public class UnsupportedServiceException extends GroupException {

	private static final long serialVersionUID = -8332539718709043101L;

	/**
	 * 
	 * Creates a new UnsupportedServiceException.
	 */
	public UnsupportedServiceException() {
		super();
	}

	/**
	 * 
	 * Creates a new UnsupportedServiceException.
	 * @param "" the error message.
	 */
	public UnsupportedServiceException(Service service) {
		super("unsupported service exception: "+service);
	}

}
