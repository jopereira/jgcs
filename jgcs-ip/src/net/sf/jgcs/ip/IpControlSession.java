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

public class IpControlSession extends AbstractControlSession<IpProtocol,IpDataSession,IpControlSession,IpGroup> {
	private MulticastSocket sock;
	private boolean joined;

	public IpControlSession(MulticastSocket sock) {
		this.sock=sock;
		this.joined=false;
	}

	public void join() throws GroupException {
		lock.lock();
		onEntry();
		try {
			sock.joinGroup(group.getGroupAddress());
			joined = true;
		} catch (IOException e) {
			throw new InvalidStateException("cannot join group", e);
		} finally {
			lock.unlock();
		}
	}

	public void leave() throws GroupException {
		lock.lock();
		onEntry();
		try {
			joined = false;
			sock.leaveGroup(group.getGroupAddress());
		} catch (IOException e) {
			throw new InvalidStateException("cannot leave group", e);
		} finally {
			lock.unlock();
		}
	}

	public SocketAddress getLocalAddress() {
		return sock.getLocalSocketAddress();
	}

	public boolean isJoined() {
		return joined;
	}
}
