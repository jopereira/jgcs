
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
 * This class defines a UnsupportedServiceException.
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public class UnsupportedServiceException extends JGCSException {

	private static final long serialVersionUID = -1665250751731240056L;

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
	 * @param message the error message.
	 */
	public UnsupportedServiceException(String message) {
		super(message);
	}

	/**
	 * 
	 * Creates a new UnsupportedServiceException.
	 * @param message the error message.
	 * @param cause the throwable that caused this exception.
	 */
	public UnsupportedServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
