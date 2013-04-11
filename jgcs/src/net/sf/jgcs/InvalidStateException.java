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
 * The operation cannot be performed in the current group state.
 * 
 * This happens when trying to re-join or re-leave a group, sending
 * messages to a blocked group, sending messaages to a closed group
 * without joining, ... 
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public class InvalidStateException extends GroupException {

	private static final long serialVersionUID = 5069170460259403046L;

	/**
	 * 
	 * Creates a new NotJoinedException.
	 */
	public InvalidStateException() {
		super();
	}

	/**
	 * 
	 * Creates a new NotJoinedException.
	 * @param s the error message
	 */
	public InvalidStateException(String s) {
		super(s);
	}

	/**
	 * 
	 * Creates a new NotJoinedException.
	 * @param s the error message.
	 * @param cause the throwable that caused this exception.
	 */
	public InvalidStateException(String s, Throwable cause) {
		super(s, cause);
	}
}
