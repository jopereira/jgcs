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
 * 
 * This class defines a ServiceListener.
 * 
 * Listeners interested in receiving notifications about guarantees of requested services
 * on messages must implement this interface.
 * 
 * @see DataSession
 * @see Service
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public interface ServiceListener extends MessageListener {
	
	/**
	 * Notifies the application that one certain service to a message delivery is already ensured.
	 * The message is identified by the context. This context must be previously provided by the application. 
	 * 
	 *  @param context context previously provided by the application that identifies a message.
	 *  @param service service ensured.
	 */
	public void onServiceEnsured(Object context, Service service);
}
