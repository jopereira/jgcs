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
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import net.sf.jgcs.Membership;
import net.sf.jgcs.MembershipID;
import net.sf.jgcs.spread.jni.Mailbox;

public class SpMembership implements Membership {
	private Mailbox.ViewInfo view;
	private Mailbox.ReceiveArgs info;
	private int currentRank;

	public SpMembership(String id, Mailbox.ReceiveArgs info, Mailbox.ViewInfo view) {
		this.view=view;
		this.info=info;
		this.currentRank=findRank(id, info.groups);
	}
	
	private static int findRank(String id, String[] members) {
		for(int i=0;i<members.length;i++)
			if (members[i].equals(id))
				return i;
		return -1;
	}

	public List<SocketAddress> getMembershipList() {
		List<SocketAddress> view=new ArrayList<SocketAddress>(info.groups.length);
		for(String s: info.groups)
			view.add(new SpGroup(s));
		return view;
	}

	public MembershipID getMembershipID() {
		return new SpMembershipID(view.group_id);
	}

	public int getLocalRank() {
		return currentRank;
	}

	public int getCoordinatorRank() {
		return 0;
	}

	public int getMemberRank(SocketAddress peer) {
		return findRank(((SpGroup)peer).getGroup(), info.groups);
	}

	public SocketAddress getMemberAddress(int rank) {
		return new SpGroup(info.groups[rank]);
	}

	public List<SocketAddress> getJoinedMembers() {
		List<SocketAddress> res=new LinkedList<SocketAddress>();
		if ((info.service_type&SpService.CAUSED_BY_JOIN)!=0)
			res.add(new SpGroup(view.vs_set[0]));
		return res;
	}

	public List<SocketAddress> getLeavedMembers() {
		List<SocketAddress> res=new LinkedList<SocketAddress>();
		if ((info.service_type&SpService.CAUSED_BY_LEAVE)!=0)
			res.add(new SpGroup(view.vs_set[0]));
		return res;
	}

	public List<SocketAddress> getFailedMembers() {
		List<SocketAddress> res=new LinkedList<SocketAddress>();
		if ((info.service_type&SpService.CAUSED_BY_DISCONNECT)!=0)
			res.add(new SpGroup(view.vs_set[0]));
		return res;
	}
}
