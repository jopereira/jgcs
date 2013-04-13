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
 * A group membership configuration. This includes the current members
 * of the group, but also how it differs from the previous configuration. 
 */
public interface Membership {

	/**
	 * Gets the list of members in this configuration.
	 * @return list of members.
	 */
	public List<SocketAddress> getMembershipList();

	/**
	 * Gets the membership identifier
	 * @return a membership identifier.
	 */
	public MembershipID getMembershipID();

	/**
	 * Gets the rank of this process in this membership.
	 * @return the rank of the local process.
	 * @throws InvalidStateException if the member is not in a group.
	 */
	public int getLocalRank() throws InvalidStateException;

	/**
	 * Gets the rank of the coordinator of this group. It is guaranteed
	 * that all members will get the same reply.
	 * @return the rank of the coordinator of the group.
	 */
	public int getCoordinatorRank();

	/**
	 * Gets the member rank that has the given address, or null if there is no matching rank.
	 * @param member the socket address of the member.
	 * @return the rank of the member.
	 */
	public int getMemberRank(SocketAddress member);

	/**
	 * Gets the address of the member that has the given rank.
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