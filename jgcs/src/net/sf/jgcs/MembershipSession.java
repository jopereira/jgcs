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
 * Allows the application to know the current group composition. This
 * is used by protocols implementing group membership services, that
 * agree on a common view of the group and notify the application
 * on membership changes.
 */
public interface MembershipSession extends ControlSession {

	/**
	 * Gets the current membership.
	 * @return a membership.
	 * @throws InvalidStateException if the member is not joined
	 */
	public Membership getMembership() throws InvalidStateException;

	/**
	 * Gets the current membership identifier
	 * @return a membership identifier
	 * @throws InvalidStateException if the member is not joined
	 */
	public MembershipID getMembershipID() throws InvalidStateException;
	
	/**
	 * Registers a listener for the membership changes.
	 * @param listener the listener to register.
	 */
	public void setMembershipListener(MembershipListener listener);
	
	/**
	 * Get the registered listener.
	 * @return the current listener.
	 */
	public MembershipListener getMembershipListener();
}
