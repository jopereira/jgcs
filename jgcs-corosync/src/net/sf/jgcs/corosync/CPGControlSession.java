/*
 * Corosync/CPG implementation of JGCS - Group Communication Service.
 * Copyright (C) 2013 Universidade do Minho
 *
 * jop@di.uminho.pt - http://www.di.uminho.pt/~jop
 *
 * Departamento de Informatica, Universidade do Minho
 * Campus de Gualtar, 4710-057 Braga, Portugal
 *
 * See COPYING for licensing details.
 */
	  
package net.sf.jgcs.corosync;

import java.net.SocketAddress;

import net.sf.jgcs.JGCSException;
import net.sf.jgcs.spi.AbstractMembershipSession;

public class CPGControlSession extends AbstractMembershipSession<CPGProtocol,CPGDataSession,CPGControlSession,CPGGroup> {
	private SocketAddress localid;

	@Override
	public synchronized void join() throws JGCSException {
		onEntry();
		protocol.cpg.join(group.getGroup());
	}

	@Override
	public synchronized void leave() throws JGCSException {
		onEntry();
		protocol.cpg.leave(group.getGroup());
	}

	@Override
	public boolean isJoined() {
		return localid!=null;
	}

	@Override
	public SocketAddress getLocalAddress() {
		return localid;
	}

	void install(CPGAddress[] members, CPGAddress[] left, int[] lr, CPGAddress[] joined, int[] jr) {
		CPGMembership memb = new CPGMembership(localid, members, left, lr, joined);
		notifyAndSetMembership(memb);
	}
}
