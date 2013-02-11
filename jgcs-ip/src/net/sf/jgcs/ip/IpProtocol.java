
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

import java.io.IOException;
import java.net.MulticastSocket;

import net.sf.jgcs.AbstractProtocol;
import net.sf.jgcs.ControlSession;
import net.sf.jgcs.DataSession;
import net.sf.jgcs.GroupConfiguration;
import net.sf.jgcs.JGCSException;


public class IpProtocol extends AbstractProtocol {
	private synchronized void createSessions(IpGroup group) throws JGCSException {
		MulticastSocket sock;
		try {
			sock=new MulticastSocket(group.getPort());
		} catch (IOException e) {
			throw new JGCSException("protocol exception", e);
		}
		putSessions(group, new IpControlSession(sock, group), new IpDataSession(sock, this, group));
	}
	
	public DataSession openDataSession(GroupConfiguration group) throws JGCSException {
		DataSession data=lookupDataSession(group);
		if (data==null) {
			createSessions((IpGroup)group);
			data=lookupDataSession(group);
		}
		return data;
	}

	public ControlSession openControlSession(GroupConfiguration group) throws JGCSException {
		ControlSession control=lookupControlSession(group);
		if (control==null) {
			createSessions((IpGroup)group);
			control=lookupControlSession(group);
		}
		return control;
	}
}
