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

import java.io.Closeable;
import java.io.IOException;

/**
 * 
 * This class defines a DataSession.
 * 
 * This Session must be used to send and receive messages to/from the group. An instance of a DataSession
 * must be created on the Protocol interface.
 * 
 * @see Protocol
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public interface DataSession extends Closeable {

	/**
	 * Gets the group associated with this session.
	 * @return the group associated with this session.
	 */
	public GroupConfiguration getGroup();
	
	/**
	 * Adds a listener to deliver messages from this channel. 
	 * 
	 * @param listener The listener to be bound to the channel.
	 */
	public void setMessageListener(MessageListener listener)
		throws ClosedSessionException;
	
	/**
	 * Get the registered message listener.
	 * @return the current listener.
	 */
	public MessageListener getMessageListener();
	
	/**
	 * Adds a listener to deliver notifications from this channel.
	 * @param listener the listener to be bound to the channel.
	 */
	public void setServiceListener(ServiceListener listener)
		throws ClosedSessionException;
	
	/**
	 * Get the registered service listener.
	 * @return the current listener.
	 */
	public ServiceListener getServiceListener();
	
	/**
	 * Closes the session. All resources that the session holds
	 * should be freed and therefore no subsequent communication
	 * can be done.
	 */
	@Override
	public void close() throws IOException;
	
	/**
	 * Check if this session has been closed.
	 * @return true if already closed
	 */
	public boolean isClosed();

	/**
	 * Creates an empty message that can be used (transmitted) through the session.
	 * 
	 * @return The message created.
	 */
	public Message createMessage() throws GroupException;
		
	/**
	 * <p>Sends a message to the group.</p>
	 * @param msg The message to be sent.
	 * @param service the service needed by the application for message delivery (e.g. total order)
	 * or null to use the default channel service.
	 * @param cookie a cookie used to identify the message in the future (e.g. service notifications).
	 * @param annotation semantic information provided by the application 
	 * to be used by communication protocols (e.g. semantic reliability).
	 * @throws IOException 
	 */
	public void multicast(Message msg, Service service, Object cookie, Annotation... annotation) throws IOException;
}
