
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
 * This class defines a ClosedSessionException. 
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public class ClosedSessionException extends JGCSException {

	private static final long serialVersionUID = 3204404038042076904L;

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
