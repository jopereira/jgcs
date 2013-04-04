
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
public class UnsupportedGroupException extends GroupException {

	private static final long serialVersionUID = 4206652425727671499L;

	/**
	 * 
	 * Creates a new UnsupportedServiceException.
	 */
	public UnsupportedGroupException() {
		super();
	}

	/**
	 * 
	 * Creates a new UnsupportedServiceException.
	 * @param group the error message.
	 */
	public UnsupportedGroupException(GroupConfiguration group) {
		super("unsupported group configuration: "+group);
	}

}
