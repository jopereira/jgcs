/*
 * NeEM implementation of JGCS - Group Communication Service.
 * Copyright (C) 2006,2013 José Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
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
