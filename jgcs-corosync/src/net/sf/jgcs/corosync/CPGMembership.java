/*
 * Corosync/CPG implementation of JGCS - Group Communication Service.
 * Copyright (C) 2013 José Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs.corosync;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.jgcs.Membership;
import net.sf.jgcs.MembershipID;
import net.sf.jgcs.InvalidStateException;
import net.sf.jgcs.corosync.jni.ClosedProcessGroup;

class CPGMembership implements Membership {
	
	private List<SocketAddress> members;
	private List<SocketAddress> left = new ArrayList<SocketAddress>(), failed = new ArrayList<SocketAddress>();
	private List<SocketAddress> joined;
	private SocketAddress localid;

	CPGMembership(SocketAddress localid, CPGAddress[] members, CPGAddress[] left, int[] lr, CPGAddress[] joined) {
		this.localid = localid;
		this.members = Arrays.asList((SocketAddress[])members);
		this.joined = Arrays.asList((SocketAddress[])joined);
		for(int i = 0; i<left.length; i++) {
			if (lr[i] == ClosedProcessGroup.CPG_REASON_LEAVE)
				this.left.add(left[i]);
			else
				failed.add(left[i]);
		}
	}

	@Override
	public List<SocketAddress> getMembershipList() {
		return members;
	}

	@Override
	public MembershipID getMembershipID() {		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLocalRank() throws InvalidStateException {
		return getMemberRank(localid);
	}

	@Override
	public int getCoordinatorRank() {
		return 0;
	}

	@Override
	public int getMemberRank(SocketAddress peer) {
		return members.indexOf(peer);
	}

	@Override
	public SocketAddress getMemberAddress(int rank) {
		return members.get(rank);
	}

	@Override
	public List<SocketAddress> getJoinedMembers() {
		return joined;
	}

	@Override
	public List<SocketAddress> getLeavedMembers() {
		return left;
	}

	@Override
	public List<SocketAddress> getFailedMembers() {
		return failed;
	}
	
	@Override
	public String toString() {
		return "view-"+members;
	}
}
