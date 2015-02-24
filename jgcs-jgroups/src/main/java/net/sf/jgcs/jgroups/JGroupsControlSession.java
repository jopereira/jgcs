/*
 * JGroups implementation of JGCS - Group Communication Service
 * Copyright (C) 2006 Nuno Carvalho, Universidade de Lisboa
 * Copyright (C) 2013 José Orlando Pereira
 * 
 * http://github.com/jopereira/jgcs
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
 
package net.sf.jgcs.jgroups;

import java.net.SocketAddress;

import net.sf.jgcs.ClosedSessionException;
import net.sf.jgcs.GroupException;
import net.sf.jgcs.InvalidStateException;
import net.sf.jgcs.spi.AbstractMembershipSession;

import org.jgroups.JChannel;
import org.jgroups.View;

class JGroupsControlSession extends AbstractMembershipSession<JGroupsProtocol,JGroupsDataSession,JGroupsControlSession,JGroupsGroup> {

	private JChannel channel;

	JGroupsControlSession(JChannel ch) {
		super();
		this.channel = ch;
	}

	@Override
	protected void cleanup() {
		super.cleanup();
		channel.close();
	}
	
	@Override
	public void join() throws GroupException {
		lock.lock();
		if (isJoined())
			throw new InvalidStateException();
		try {
			channel.connect(group.getGroupName());
		} catch(IllegalStateException ise) {
			throw new ClosedSessionException("channel closed", ise);
		} catch (Exception e) {
			throw new GroupException("Could not connect JGroups Channel.",e);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void leave() throws GroupException {
		try {
			lock.lock();
			onEntry();
			if (!isJoined())
				throw new InvalidStateException();
			channel.disconnect();
			setMembership(null);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public SocketAddress getLocalAddress() {
		if (channel == null || channel.getAddress() == null)
			return null;
		return new JGroupsSocketAddress(channel.getAddress());
	}

	// listeners of JGroups
	
	protected void jgroupsViewAccepted(View new_view) {
		JGroupsSocketAddress addr = new JGroupsSocketAddress(channel.getAddress());
		JGroupsMembership incomingMembership = new JGroupsMembership(addr, new_view, membership);
		if (incomingMembership.getMembershipList().contains(addr))
			notifyAndSetMembership(incomingMembership);
		else
			notifyRemoved();
	}

	private boolean isJoined() {
		try {
			return getMembership() != null;
		} catch (InvalidStateException e) {
			return false;
		}
	}
}
