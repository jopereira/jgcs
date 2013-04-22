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
import java.net.SocketAddress;

import net.sf.jgcs.InvalidStateException;
import net.sf.jgcs.GroupException;
import net.sf.jgcs.spi.AbstractControlSession;

class IpControlSession extends AbstractControlSession<IpProtocol,IpDataSession,IpControlSession,IpGroup> {
	private MulticastSocket sock;

	IpControlSession(MulticastSocket sock) {
		this.sock=sock;
	}

	@Override
	public void join() throws GroupException {
		lock.lock();
		onEntry();
		try {
			sock.joinGroup(group.getAddress().getAddress());
		} catch (IOException e) {
			throw new InvalidStateException("cannot join group", e);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void leave() throws GroupException {
		lock.lock();
		onEntry();
		try {
			sock.leaveGroup(group.getAddress().getAddress());
		} catch (IOException e) {
			throw new InvalidStateException("cannot leave group", e);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public SocketAddress getLocalAddress() {
		return sock.getLocalSocketAddress();
	}
}
