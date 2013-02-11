
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
 * This class defines a DataSessionException.
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public class DataSessionException extends JGCSException {

	private static final long serialVersionUID = -5456368119040106302L;

	/**
	 * 
	 * Creates a new DataSessionException.
	 */
	public DataSessionException() {
		super();
	}

	/**
	 * 
	 * Creates a new DataSessionException.
	 * @param message the error message.
	 */
	public DataSessionException(String message) {
		super(message);
	}

	/**
	 * 
	 * Creates a new DataSessionException.
	 * @param message the error message
	 * @param cause the thowable that caused this exception.
	 */
	public DataSessionException(String message, Throwable cause) {
		super(message, cause);
	}

}
