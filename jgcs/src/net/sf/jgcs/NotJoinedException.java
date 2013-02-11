
/*
 * JGCS - Group Communication Service.
 * Copyright (C) 2006 Nuno Carvalho, Universidade de Lisboa
 *
 * jgcs@lasige.di.fc.ul.pt
 *
 * Departamento de Informatica, Universidade de Lisboa
 * Bloco C6, Faculdade de Ciências, Campo Grande, 1749-016 Lisboa, Portugal.
 *
 * See COPYING for licensing details.
 */
package net.sf.jgcs;

/**
 * 
 * This class defines a NotJoinedException.
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public class NotJoinedException extends JGCSException {

	private static final long serialVersionUID = -7408610280397915892L;

	/**
	 * 
	 * Creates a new NotJoinedException.
	 */
	public NotJoinedException() {
		super();
	}

	/**
	 * 
	 * Creates a new NotJoinedException.
	 * @param s the error message
	 */
	public NotJoinedException(String s) {
		super(s);
	}

	/**
	 * 
	 * Creates a new NotJoinedException.
	 * @param s the error message.
	 * @param cause the throwable that caused this exception.
	 */
	public NotJoinedException(String s, Throwable cause) {
		super(s, cause);
	}

	/**
	 * 
	 * Creates a new NotJoinedException.
	 * @param s the error message.
	 * @param code the error code.
	 */
	public NotJoinedException(String s, int code) {
		super(s, code);
	}

	/**
	 * 
	 * Creates a new NotJoinedException.
	 * @param s the error message.
	 * @param cause the throwable that caused this exception.
	 * @param code the error code.
	 */
	public NotJoinedException(String s, Throwable cause, int code) {
		super(s, cause, code);
	}

}
