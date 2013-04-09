/*
 * Corosync/CPG implementation of JGCS - Group Communication Service.
 * Copyright (C) 2013 Universidade do Minho
 *
 * jop@di.uminho.pt - http://www.di.uminho.pt/~jop
 *
 * Departamento de Informatica, Universidade do Minho
 * Campus de Gualtar, 4710-057 Braga, Portugal
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs.corosync;

import net.sf.jgcs.GroupException;
import net.sf.jgcs.Protocol;
import net.sf.jgcs.ProtocolFactory;

public class CPGProtocolFactory implements ProtocolFactory {
	
	private static final long serialVersionUID = 2L;

	public Protocol createProtocol() throws GroupException {
		return new CPGProtocol();
	}
}
