/*
 * NeEM implementation of JGCS - Group Communication Service.
 * Copyright (C) 2006 Jose' Orlando Pereira, Universidade do Minho
 *
 * jop@di.uminho.pt - http://gsd.di.uminho.pt/~jop
 * jgcs@lasige.di.fc.ul.pt
 *
 * Departamento de Informatica, Universidade do Minho
 * Campus de Gualtar, 4710-057 Braga, Portugal
 *
 * See COPYING for licensing details.
 */
	  
package net.sf.jgcs.neem;

import net.sf.jgcs.Service;

public class NeEMService implements Service {
	
	private static final long serialVersionUID = 2L;
	
	public boolean satisfies(Service service) {
		return service instanceof NeEMService;
	}
}
