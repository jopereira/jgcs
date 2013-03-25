
/*
 * IP Multicast implementation of JGCS - Group Communication Service.
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
	  
package net.sf.jgcs.ip;

import net.sf.jgcs.JGCSException;
import net.sf.jgcs.Protocol;
import net.sf.jgcs.ProtocolFactory;

public class IpProtocolFactory implements ProtocolFactory {

	private static final long serialVersionUID = 2L;

	public Protocol createProtocol() throws JGCSException{
		return new IpProtocol();
	}
}
