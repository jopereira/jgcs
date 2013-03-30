
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

import net.sf.jgcs.ClosedSessionException;
import net.sf.jgcs.JGCSException;
import net.sf.jgcs.NotJoinedException;
import net.sf.jgcs.spi.AbstractMembershipSession;

import org.apache.log4j.Logger;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.View;

public class JGroupsControlSession extends AbstractMembershipSession<JGroupsProtocol,JGroupsDataSession,JGroupsControlSession,JGroupsGroup> {

	private static Logger logger = Logger.getLogger(JGroupsControlSession.class);

	private JChannel channel;

	public JGroupsControlSession(JChannel ch) {
		super();
		this.channel = ch;
	}

	public void join() throws JGCSException {
		try {
			channel.connect(group.getGroupName());
		} catch (Exception e) {
			throw new JGCSException("Could not connect JGroups Channel.",e);
		}
	}

	public void leave() throws ClosedSessionException, JGCSException {
		channel.disconnect();
		setMembership(null);
	}

	public SocketAddress getLocalAddress() {
		if (channel == null)
			return null;
		return new JGroupsSocketAddress(channel.getAddress());
	}

	// listeners of JGroups
	
	public void jgroupsViewAccepted(View new_view) {
	    //JGroups BugWorkAround: Do Not Remove This String Representation Of The View 
		new_view.toString();
		JGroupsMembership currentMembership = null;
		try {
			currentMembership = (JGroupsMembership) getMembership();
		} catch (NotJoinedException e) {
			// this means that is the first view
		}
		JGroupsMembership incomingMembership = null;
		if(currentMembership == null){
			// if this is the first view
			incomingMembership = new JGroupsMembership(new_view,channel);
			incomingMembership.setJoined(incomingMembership.getMembershipList());
			notifyAndSetMembership(incomingMembership);
		}
		else{
			incomingMembership = new JGroupsMembership(new_view,channel);
			List<SocketAddress> newMembersList = newMembers(currentMembership,incomingMembership);
			List<SocketAddress> oldMembersList = oldMembers(currentMembership,incomingMembership);
			incomingMembership.setFailed(currentMembership.getFailedToNextView());
			incomingMembership.setLeaved(oldMembersList);
			incomingMembership.setJoined(newMembersList);
			if(logger.isDebugEnabled())
				logger.debug("Old: "+oldMembersList+" -> new: "+newMembersList);
			
			notifyAndSetMembership(incomingMembership);
			
		}
		if(logger.isDebugEnabled())
			logger.debug("View Accepted! currentMembership: "+currentMembership);
	}

	private List<SocketAddress> 
	newMembers(JGroupsMembership oldMembership, JGroupsMembership newMembership){
		
		List<SocketAddress> newMembersList = new ArrayList<SocketAddress>();
		for(SocketAddress peer : newMembership.getMembershipList())
			if(!oldMembership.getMembershipList().contains(peer))
				newMembersList.add(peer);
		return newMembersList;
	}
	
	private List<SocketAddress> 
	oldMembers(JGroupsMembership oldMembership, JGroupsMembership newMembership){
		
		List<SocketAddress> oldMembersList = new ArrayList<SocketAddress>();
		for(SocketAddress peer : oldMembership.getMembershipList())
			if(!newMembership.getMembershipList().contains(peer) && 
					!oldMembership.getFailedMembers().contains(peer))
				oldMembersList.add(peer);
		return oldMembersList;
	}

	public void jgroupsSuspect(Address suspected_mbr) {
		if(logger.isDebugEnabled())
			logger.debug("suspected member "+suspected_mbr);
		SocketAddress peer = new JGroupsSocketAddress(suspected_mbr);
		JGroupsMembership m = null;
		try {
			m = (JGroupsMembership) getMembership();
		} catch (NotJoinedException e) {
			logger.warn("Received notification of suspected member, but I'm not joined.",e);
			return;
		}
		m.addToFailed(peer);
	}

	public boolean isJoined() {
		try {
			return getMembership() != null;
		} catch (NotJoinedException e) {
			return false;
		}
	}
}
