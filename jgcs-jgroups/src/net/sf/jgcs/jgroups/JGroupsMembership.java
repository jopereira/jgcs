
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
import java.util.Iterator;
import java.util.List;


import net.sf.jgcs.NotJoinedException;
import net.sf.jgcs.membership.Membership;
import net.sf.jgcs.membership.MembershipID;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.View;

public class JGroupsMembership implements Membership {

	private List<SocketAddress> addresses;
	private List<SocketAddress> failed, joined, leaved, failedOnNextView;
	private SocketAddress myAddr;
	private JGroupsMembershipID membershipID;
	
	public JGroupsMembership(View view, JChannel ch) {
		addresses = new ArrayList<SocketAddress>();
		Iterator it = view.getMembers().iterator();
		while(it.hasNext()){
			Address jgroupsAddr = (Address) it.next();
			addresses.add(new JGroupsSocketAddress(jgroupsAddr));
		}
		membershipID = new JGroupsMembershipID(view.getVid().getCoordAddress(),view.getVid().getId());
		Address myjgaddr = (Address) ch.getLocalAddress();
		myAddr = new JGroupsSocketAddress(myjgaddr);
		failed = new ArrayList<SocketAddress>(addresses.size());
		joined = new ArrayList<SocketAddress>(addresses.size());
		leaved = new ArrayList<SocketAddress>(addresses.size());
		failedOnNextView = new ArrayList<SocketAddress>(addresses.size());
	}

	public MembershipID getMembershipID() {
		return membershipID;
	}

	SocketAddress getMyAddress() {
		return myAddr;
	}
	
	public String toString(){
		return "JGroups membership: "+addresses;
	}
	
	void setFailed(List<SocketAddress> failed) {
		this.failed = failed;
	}

	void setJoined(List<SocketAddress> joined) {
		this.joined = joined;
	}

	void setLeaved(List<SocketAddress> leaved) {
		this.leaved = leaved;
	}

	public boolean addToFailed(SocketAddress peer){
		boolean contain = addresses.contains(peer);
		if(contain)
			failedOnNextView.add(peer);
		return contain;
	}
	
	public List<SocketAddress> getFailedToNextView() {
		return failedOnNextView;
	}

	public List<SocketAddress> getMembershipList() {
		return addresses;
	}

	public int getLocalRank() throws NotJoinedException {
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
