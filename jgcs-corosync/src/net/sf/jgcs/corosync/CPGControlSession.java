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

import net.sf.jgcs.ClosedSessionException;
import net.sf.jgcs.GroupException;
import net.sf.jgcs.InvalidStateException;
import net.sf.jgcs.corosync.jni.CorosyncException;
import net.sf.jgcs.spi.AbstractMembershipSession;

public class CPGControlSession extends AbstractMembershipSession<CPGProtocol,CPGDataSession,CPGControlSession,CPGGroup> {
	@Override
	public void join() throws GroupException {
		try {
			protocol.cpg.join(group.getGroup());
		} catch(NullPointerException npe) {
			throw new ClosedSessionException();
		} catch(CorosyncException ce) {
			if (ce.getError() == CorosyncException.BAD_HANDLE) {
				throw new ClosedSessionException();
			}
			if (ce.getError() == CorosyncException.EXIST ||
				ce.getError() == CorosyncException.TRY_AGAIN) {
				throw new InvalidStateException("cannot join now");
			}
			throw ce;
		}
	}

	@Override
	public void leave() throws GroupException {
		try {
			protocol.cpg.leave(group.getGroup());
		} catch(NullPointerException npe) {
			throw new ClosedSessionException();
		} catch(CorosyncException ce) {
			if (ce.getError() == CorosyncException.BAD_HANDLE) {
				throw new ClosedSessionException();
			}
			if (ce.getError() == CorosyncException.NOT_EXIST ||
				ce.getError() == CorosyncException.TRY_AGAIN) {
				throw new InvalidStateException("cannot leave now");
			}
			throw ce;
		}
	}

	@Override
	public SocketAddress getLocalAddress() {
		try {
			lock.lock();
			if (isClosed())
				return null;
			return new CPGAddress(protocol.cpg.getLocalNodeId(), protocol.cpg.getProcessId());
		} catch (CorosyncException ce) {
			return null;
		} finally {
			lock.unlock();
		}
	}

	void install(CPGAddress[] members, CPGAddress[] left, int[] lr, CPGAddress[] joined, int[] jr) {
		CPGMembership memb = new CPGMembership(getLocalAddress(), members, left, lr, joined);
		notifyAndSetMembership(memb);
	}
}
