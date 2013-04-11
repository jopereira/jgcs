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

import java.io.IOException;

/**
 * This class defines a JGCSException.
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public class GroupException extends IOException {

	private static final long serialVersionUID = 1456805404115290712L;

	/**
	 * 
	 * Creates a new JGCSException.
	 */
	public GroupException() {
		super();
	}

	/**
	 * 
	 * Creates a new JGCSException.
	 * @param s the error message.
	 */
	public GroupException(String s) {
		super(s);
	}

	/**
	 * 
	 * Creates a new JGCSException.
	 * @param s the error message.
	 * @param cause the throwable that caused this exception.
	 */
	public GroupException(String s, Throwable cause) {
		super(s, cause);
	}
}
