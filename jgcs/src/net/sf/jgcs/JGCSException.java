
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

import java.io.IOException;

/**
 * This class defines a JGCSException.
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public class JGCSException extends IOException {

	private static final long serialVersionUID = 5024598044594867669L;
	private int errorCode;

	/**
	 * 
	 * Creates a new JGCSException.
	 */
	public JGCSException() {
		super();
	}

	/**
	 * 
	 * Creates a new JGCSException.
	 * @param s the error message.
	 */
	public JGCSException(String s) {
		super(s);
	}

	/**
	 * 
	 * Creates a new JGCSException.
	 * @param s the error message.
	 * @param cause the throwable that caused this exception.
	 */
	public JGCSException(String s, Throwable cause) {
		super(s);
		this.initCause(cause);
	}

	/**
	 * 
	 * Creates a new JGCSException.
	 * @param s the error message.
	 * @param code the error code.
	 */
	public JGCSException(String s, int code) {
		super(s);
		errorCode = code;
	}

	/**
	 * 
	 * Creates a new JGCSException.
	 * @param s the error message
	 * @param cause the throwable that caused this exception.
	 * @param code the error code
	 */
	public JGCSException(String s, Throwable cause, int code) {
		super(s);
		this.initCause(cause);
		errorCode = code;
	}

	/**
	 * Gets the throwable that caused this exception.
	 */
	public Throwable getCause(){
		return super.getCause();
	}
	
	/**
	 * Gets the error code that identifies the error ocurred.
	 * @return the error code.
	 */
	public int getErrorCode(){
		return errorCode;
	}
	
}
