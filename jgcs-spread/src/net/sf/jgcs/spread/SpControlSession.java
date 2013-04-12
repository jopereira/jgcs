/*
 * Spread implementation of JGCS - Group Communication Service.
 * Copyright (C) 2006,2013 Jose' Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 * 
 * See COPYING for licensing details.
 */
	  
package net.sf.jgcs.spread;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

import net.sf.jgcs.ClosedSessionException;
import net.sf.jgcs.GroupException;
import net.sf.jgcs.InvalidStateException;
import net.sf.jgcs.MembershipID;
import net.sf.jgcs.spi.AbstractBlockSession;
import net.sf.jgcs.spread.jni.Mailbox;

public class SpControlSession extends AbstractBlockSession<SpProtocol,SpDataSession,SpControlSession,SpGroup> {
	private Mailbox mb;
	private boolean blocked = true;
	private SpMembership current;
	private boolean joining;

	SpControlSession(Mailbox mb) {
		this.mb = mb;
	}

	@Override
	public void join() throws GroupException {
		try {
			lock.lock();
			if (joining)
				throw new InvalidStateException();
			joining=true;
		} finally {
			lock.unlock();
		}
		int ret=mb.C_join(group.getGroup());
		if (ret<0) {
			if (ret == SpException.ILLEGAL_SESSION)
				throw new ClosedSessionException();
			throw new SpException(ret, null);
		}
	}

	@Override
	public void leave() throws GroupException {
		try {
			lock.lock();
			if (!joining)
				throw new InvalidStateException();
			joining=false;
		} finally {
			lock.unlock();
		}
		int ret=mb.C_leave(group.getGroup());
		if (ret<0) {
			if (ret == SpException.ILLEGAL_SESSION)
				throw new ClosedSessionException();
			throw new SpException(ret, null);
		}
	}

	@Override
	public SocketAddress getLocalAddress() {
		try {
			lock.lock();
			if (isClosed())
				return null;
			return new SpGroup(mb.getPrivateGroup());
		} finally {
			lock.unlock();
		}
	}

	protected void deliverView(Mailbox.ReceiveArgs info, ByteBuffer mess) {
		if ((info.service_type & SpService.TRANSITION_MESS)!=0) {
			// TODO ignoring transitional views...
			return;
		} else if ((info.service_type & SpService.FLUSH_REQ_MESS) != 0) {
            this.notifyBlock();
            return;
		} else if ((info.service_type & SpService.REG_MEMB_MESS)==0) {
			notifyRemoved();
			current=null;
			return;
		}
		blocked = false;
		Mailbox.ViewInfo view = new Mailbox.ViewInfo();
		int ret=mb.C_parseView(view, info, mess);
		if (ret<0)
			throw new IllegalArgumentException("buffer too short");

		current=new SpMembership(mb.getPrivateGroup(), info, view);
		notifyAndSetMembership(current);		
	}

	@Override
	public void blockOk() throws InvalidStateException, GroupException {
		int ret=mb.C_flush(group.getGroup());
		if (ret<0) throw new SpException(ret, null);
		blocked = true;
	}

	@Override
	public boolean isBlocked() throws InvalidStateException {
		return blocked;
	}

	@Override
	public MembershipID getMembershipID() throws InvalidStateException {
		return current.getMembershipID();
	}
}
