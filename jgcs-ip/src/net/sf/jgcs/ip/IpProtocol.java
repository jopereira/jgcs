/*
 * IP Multicast implementation of JGCS - Group Communication Service.
 * Copyright (C) 2006,2013 José Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 *
 * See COPYING for licensing details.
 */
	  
package net.sf.jgcs.ip;

import java.io.IOException;
import java.net.MulticastSocket;

import net.sf.jgcs.GroupException;
import net.sf.jgcs.spi.AbstractProtocol;

public class IpProtocol extends AbstractProtocol<IpProtocol,IpDataSession,IpControlSession,IpGroup> {
	protected void createSessions(IpGroup group) throws GroupException {
		MulticastSocket sock;
		try {
			sock=new MulticastSocket(group.getPort());
		} catch (IOException e) {
			throw new GroupException("protocol exception", e);
		}
		putSessions(group, new IpControlSession(sock), new IpDataSession(sock));
	}	
}
