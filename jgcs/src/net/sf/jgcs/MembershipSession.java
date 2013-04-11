
/*
 * JGCS - Group Communication Service.
 * Copyright (C) 2006 Nuno Carvalho, Universidade de Lisboa
 *
 * jgcs@lasige.di.fc.ul.pt
 *
 * Departamento de Informatica, Universidade de Lisboa
 * Bloco C6, Faculdade de Ciências, Campo Grande, 1749-016 Lisboa, Portugal.
 *
 * See COPYING for licensing details.
 */
package net.sf.jgcs;


/**
 * This class defines a MembershipSession.
 * This session should be implemented when the underlying toolkit provides
 * extended view synchrony semantics.
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public interface MembershipSession extends ControlSession {

	/**
	 * Gets the current Membership.
	 * @return a membership.
	 * @throws InvalidStateException if the member is not joined
	 */
	public Membership getMembership() throws InvalidStateException;

	/**
	 * Gets the current membership ID
	 * @return the current membership ID
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
