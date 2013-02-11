
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

import java.net.SocketAddress;

/**
 * 
 * This class defines a ControlListener.
 * This listener must be used by clients that wish to be notified of changes in the members that
 * join, leave or fail in a simple group.
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public interface ControlListener {

	/**
	 * Notification of a new member in the group.
	 * @param peer the address of the new member.
	 */
	public void onJoin(SocketAddress peer);	
	
	/**
	 * Notification of a member that leaved the group.
	 * @param peer the address of the leaved member.
	 */
	public void onLeave(SocketAddress peer);
	
	/**
	 * Notification of a member that was detected as failed.
	 * This notification means also that the member does not belong to the group any more.
	 * @param peer the address of the member that failed.
	 */
	public void onFailed(SocketAddress peer);
	
}
