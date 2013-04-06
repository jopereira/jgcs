
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

import net.sf.jgcs.GroupException;
import net.sf.jgcs.InvalidStateException;
import net.sf.jgcs.spi.AbstractControlSession;
import net.sf.neem.MulticastChannel;

public class NeEMControlSession extends AbstractControlSession<NeEMProtocol,NeEMDataSession,NeEMControlSession,NeEMGroup> {
	private MulticastChannel sock;
	private boolean joined;

	public NeEMControlSession(MulticastChannel channel) {
		this.sock=channel;
		this.joined = false;
	}

	public void join() throws GroupException {
		try {
			lock.lock();
			onEntry();
			if (joined)
				throw new InvalidStateException("already joined");
			for(InetSocketAddress peer: group.getPeers())
				sock.connect(peer);
			joined = true;
		} finally {
			lock.unlock();
		}
	}

	public void leave() throws GroupException {
		try {
			lock.lock();
			onEntry();
			if (!joined)
				throw new InvalidStateException("not joined");
			joined = false;
			sock.close();
		} finally {
			lock.unlock();
		}
	}

	public SocketAddress getLocalAddress() {
		try {
			lock.lock();
			if (!isClosed())
				return sock.getLocalSocketAddress();
			return null;
		} finally {
			lock.unlock();
		}
	}

	public boolean isJoined() {
		return joined;
	}
}
