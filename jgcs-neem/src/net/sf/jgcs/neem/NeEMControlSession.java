
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

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import net.sf.neem.MulticastChannel;
import net.sf.jgcs.JGCSException;
import net.sf.jgcs.spi.AbstractControlSession;

public class NeEMControlSession extends AbstractControlSession<NeEMProtocol,NeEMDataSession,NeEMControlSession,NeEMGroup> {
	private MulticastChannel sock;
	private boolean joined;

	public NeEMControlSession(MulticastChannel channel) {
		this.sock=channel;
		this.joined = false;
	}

	public void join() throws JGCSException {
		for(InetSocketAddress peer: group.getPeers())
			sock.connect(peer);
		joined = true;
	}

	public void leave() throws JGCSException {
		joined = false;
		//TODO??
	}

	public SocketAddress getLocalAddress() {
		return sock.getLocalSocketAddress();
	}

	public boolean isJoined() {
		return joined;
	}
}
