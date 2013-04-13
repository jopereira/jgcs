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
 * Thrown when trying to use a closed session.
 */
public class ClosedSessionException extends GroupException {

	private static final long serialVersionUID = 6541033485787104167L;

	public ClosedSessionException() {
		super();
	}

	public ClosedSessionException(String s, Throwable t) {
		super(s, t);
	}

	public ClosedSessionException(String s) {
		super(s);
	}
}
