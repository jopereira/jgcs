/*
 * Spread implementation of JGCS - Group Communication Service.
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
	  
package net.sf.jgcs.spread;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

import net.sf.jgcs.ClosedSessionException;
import net.sf.jgcs.JGCSException;
import net.sf.jgcs.MembershipID;
import net.sf.jgcs.NotJoinedException;
import net.sf.jgcs.spi.AbstractBlockSession;
import net.sf.jgcs.spread.jni.Mailbox;

public class SpControlSession extends AbstractBlockSession<SpProtocol,SpDataSession,SpControlSession,SpGroup> {
	private Mailbox mb;
	private boolean blocked = true;
	private SpMembership current;

	public SpControlSession(Mailbox mb) {
		this.mb = mb;
	}

	public synchronized void join() throws JGCSException {
		onEntry();
		int ret=mb.C_join(group.getGroup());
		if (ret<0) throw new SpException(ret, null);
	}

	public synchronized void leave() throws JGCSException {
		onEntry();
		int ret=mb.C_leave(group.getGroup());
		if (ret<0) throw new SpException(ret, null);
	}

	public synchronized SocketAddress getLocalAddress() {
		if (isClosed())
			return null;
		return new SpGroup(mb.getPrivateGroup());
	}

	public void deliverView(Mailbox.ReceiveArgs info, ByteBuffer mess) {
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

	public boolean isJoined() {
		return current != null;
	}

	public void blockOk() throws NotJoinedException, JGCSException {
		 if(!isJoined())
             throw new NotJoinedException();
		int ret=mb.C_flush(group.getGroup());
		if (ret<0) throw new SpException(ret, null);
		blocked = true;
	}

	public boolean isBlocked() throws NotJoinedException {
		return blocked;
	}

	public MembershipID getMembershipID() throws NotJoinedException {
		return current.getMembershipID();
	}
}
