
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

import net.sf.neem.MulticastChannel;
import net.sf.jgcs.ControlSession;
import net.sf.jgcs.DataSession;
import net.sf.jgcs.GroupConfiguration;
import net.sf.jgcs.JGCSException;
import net.sf.jgcs.spi.AbstractProtocol;

public class NeEMProtocol extends AbstractProtocol {
	private synchronized void createSessions(NeEMGroup group) throws JGCSException {
		MulticastChannel sock;
		try {
			sock=new MulticastChannel(group.getLocalAddress());
		} catch (IOException e) {
			throw new JGCSException("protocol exception", e);
		}
		putSessions(group, new NeEMControlSession(sock, group), new NeEMDataSession(sock, this, group));
	}
	
	public DataSession openDataSession(GroupConfiguration group) throws JGCSException {
		DataSession data=lookupDataSession(group);
		if (data==null) {
			createSessions((NeEMGroup)group);
			data=lookupDataSession(group);
		}
		return data;
	}

	public ControlSession openControlSession(GroupConfiguration group) throws JGCSException {
		ControlSession control=lookupControlSession(group);
		if (control==null) {
			createSessions((NeEMGroup)group);
			control=lookupControlSession(group);
		}
		return control;
	}
}
