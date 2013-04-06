
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

import java.io.IOException;

import net.sf.jgcs.GroupException;
import net.sf.jgcs.spi.AbstractProtocol;
import net.sf.neem.MulticastChannel;

public class NeEMProtocol extends AbstractProtocol<NeEMProtocol,NeEMDataSession,NeEMControlSession,NeEMGroup> {
	protected void createSessions(NeEMGroup group) throws GroupException {
		MulticastChannel sock;
		try {
			sock=new MulticastChannel(group.getLocalAddress());
		} catch (IOException e) {
			throw new GroupException("protocol exception", e);
		}
		putSessions(group, new NeEMControlSession(sock), new NeEMDataSession(sock));
	}	
}
