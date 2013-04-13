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
 * Thrown when there is an exception with group communication. It often
 * wraps a protocol specific exception.
 */
public class GroupException extends IOException {

	private static final long serialVersionUID = 1456805404115290712L;

	/**
	 * Creates a new exception.
	 */
	public GroupException() {
		super();
	}

	/**
	 * Creates a new exception
	 * @param s the error message.
	 */
	public GroupException(String s) {
		super(s);
	}

	/**
	 * Wraps a protocol specific exception.
	 * @param s the error message.
	 * @param cause the throwable that caused this exception.
	 */
	public GroupException(String s, Throwable cause) {
		super(s, cause);
	}
}
