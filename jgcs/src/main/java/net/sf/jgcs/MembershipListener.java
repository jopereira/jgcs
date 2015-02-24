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

/**
 * Application interface for delivering group membership change notifications. 
 */
public interface MembershipListener {
	
	/**
	 * Notification of a membership change. This should happen due to joining, leaving or
	 * failure of group members, but also because of merging or partitioning of 
	 * memberships. The new membership can be retrieved from the {@link MembershipSession}.
	 */
	public void onMembershipChange();

	/**
	 * Notification from the membership to indicate that the registered member
	 * does not belong to the group any more. This should happen when the member
	 * lost intermediate views (for instance, when using primary views) and lost some messages.
	 * After receiving this notification, the member may try to rejoin again.
	 */
	public void onExcluded();

}
