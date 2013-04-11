/*
 * JGCS - Group Communication Service.
 * Copyright (C) 2006 Nuno Carvalho, Universidade de Lisboa
 * Copyright (C) 2013 Jose' Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs;

import java.net.SocketAddress;
import java.util.List;


/**
 * This class defines a Membership.
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public interface Membership {

	/**
	 * Gets the current view of the membership.
	 * @return the current view of the membership.
	 */
	public List<SocketAddress> getMembershipList();

	/**
	 * Gets the current membership ID.
	 * @return the current membership ID.
	 */
	public MembershipID getMembershipID();

	/**
	 * Gets the local rank of the member in this membership.
	 * @return the local rank of this member.
	 * @throws InvalidStateException if the member is not in a group.
	 */
	public int getLocalRank() throws InvalidStateException;

	/**
	 * Gets the rank of the coordinator of this group.
	 * @return the rank of the coordinator of the group.
	 */
	public int getCoordinatorRank();

	/**
	 * Gets the member rank that has the given socket address, or null if there is no matching rank.
	 * @param peer the socket address of the member.
	 * @return the rank of the member.
	 */
	public int getMemberRank(SocketAddress peer);

	/**
	 * Gets the socket address of the member that has the given rank.
	 * @param rank the rank of the member.
	 * @return the socket address of the member.
	 */
	public SocketAddress getMemberAddress(int rank);
	
	/**
	 * Gets a list of members that joined the group since the previous membership.
	 * @return a list of new members or null if there are none.
	 */
	public List<SocketAddress> getJoinedMembers();
	
	/**
	 * Gets a list of members that leaved the group since the previous membership.
	 * @return a list of old members or null if there are none.
	 */
	public List<SocketAddress> getLeavedMembers();
	
	/**
	 * Gets a list of members that failed since the previous membership.
	 * @return a list of failed members or null if there are none.
	 */
	public List<SocketAddress> getFailedMembers();

}