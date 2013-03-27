
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
import java.net.SocketAddress;

import net.sf.jgcs.ClosedSessionException;
import net.sf.jgcs.JGCSException;
import net.sf.jgcs.spi.AbstractControlSession;


public class IpControlSession extends AbstractControlSession {
	private MulticastSocket sock;
	private IpGroup group;
	private boolean joined;

	public IpControlSession(MulticastSocket sock, IpGroup group) {
		this.sock=sock;
		this.group=group;
		this.joined=false;
	}

	public void join() throws JGCSException {
		try {
			sock.joinGroup(group.getGroupAddress());
			joined = true;
		} catch (IOException e) {
			throw new JGCSException("I/O exception", e);
		}
	}

	public void leave() throws ClosedSessionException, JGCSException {
		try {
			sock.leaveGroup(group.getGroupAddress());
			joined = false;
		} catch (IOException e) {
			throw new JGCSException("I/O exception", e);
		}
	}

	public SocketAddress getLocalAddress() {
		return sock.getLocalSocketAddress();
	}

	public boolean isJoined() {
		return joined;
	}
}
