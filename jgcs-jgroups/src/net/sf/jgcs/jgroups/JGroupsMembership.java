/*
 *
 * JGroups implementation of JGCS - Group Communication Service
 * Copyright (C) 2006 Nuno Carvalho, Universidade de Lisboa
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
 * 
 * Contact
 * 	Address:
 * 		LASIGE, Departamento de Informatica, Bloco C6
 * 		Faculdade de Ciencias, Universidade de Lisboa
 * 		Campo Grande, 1749-016 Lisboa
 * 		Portugal
 * 	Email:
 * 		jgcs@lasige.di.fc.ul.pt
 * 
 */
 
package net.sf.jgcs.jgroups;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import net.sf.jgcs.InvalidStateException;
import net.sf.jgcs.Membership;
import net.sf.jgcs.MembershipID;

import org.jgroups.Address;
import org.jgroups.View;

public class JGroupsMembership implements Membership {

	private List<SocketAddress> addresses;
	private List<SocketAddress> failed, joined, leaved;
	private SocketAddress myAddr;
	private JGroupsMembershipID membershipID;
	
	JGroupsMembership(SocketAddress me, View view, Membership previous) {
		membershipID = new JGroupsMembershipID(view.getViewId());
		myAddr = me;
		
		addresses = new ArrayList<SocketAddress>(view.getMembers().size());
		for(Address a: view.getMembers())
			addresses.add(new JGroupsSocketAddress(a));
		if (previous == null) {
			joined = new ArrayList<SocketAddress>(1);
			joined.add(me);
			failed = new ArrayList<SocketAddress>();
		} else {
			joined = new ArrayList<SocketAddress>(addresses);
			joined.removeAll(previous.getMembershipList());
			failed = new ArrayList<SocketAddress>(previous.getMembershipList());
			failed.removeAll(addresses);
		}
		leaved = new ArrayList<SocketAddress>();		
	}

	public MembershipID getMembershipID() {
		return membershipID;
	}

	public String toString(){
		return "JGroups membership: "+addresses;
	}
	
	public List<SocketAddress> getMembershipList() {
		return addresses;
	}

	public int getLocalRank() throws InvalidStateException {
		return getMemberRank(myAddr);
	}

	public int getCoordinatorRank() {
		return 0;
	}

	public int getMemberRank(SocketAddress peer) {
		return addresses.indexOf(peer);
	}

	public SocketAddress getMemberAddress(int rank) {
		return addresses.get(rank);
	}

	public List<SocketAddress> getJoinedMembers() {
		return joined;
	}

	public List<SocketAddress> getLeavedMembers() {
		return leaved;
	}

	public List<SocketAddress> getFailedMembers(){
		return failed;
	}
}
