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
 * This class defines a ClosedSessionException. 
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
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
