
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

import java.io.IOException;
import java.net.SocketAddress;

/**
 * 
 * This class defines a DataSession.
 * 
 * This Session must be used to send and receive messages to/from the group. An instance of a DataSession
 * must be created on the Protocol interface.
 * @assoc 1 Notifies 1 MessageListener
 * @assoc 1 Notifies 1 ServiceListener
 * @assoc 1 Notifies 1 ExceptionListener
 * @assoc 1 Creates 0..* Message

 * 
 * @see Protocol
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public interface DataSession {

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
	 * Adds a listener to deliver notifications from this channel.
	 * @param listener the listener to be bound to the channel.
	 */
	public void setServiceListener(ServiceListener listener)
		throws ClosedSessionException;
	
	/**
	 * Adds a listener to deliver exceptions related to message reception.
	 * @param exception the exception thrown by the implementation of the interface.
	 */
	public void setExceptionListener(ExceptionListener exception)
		throws ClosedSessionException;
	
	/**
	 * Closes the session. All resources that the session holds
	 * should be freed and therefore no subsequent communication
	 * can be done.
	 */
	public void close();
	
	/**
	 * Creates an empty message that can be used (transmitted) through the session.
	 * 
	 * @return The message created.
	 */
	public Message createMessage() throws ClosedSessionException;
		
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
	public void multicast(Message msg, Service service, Object cookie, Annotation... annotation) 
		throws IOException, UnsupportedServiceException;

	/**
	 * <p>Sends a message to one particular member of the group.</p>
	 * @param msg The message to be sent.
	 * @param service the service needed by the application for message delivery (e.g. total order)
	 * or null to use the default channel service.
	 * @param cookie a cookie used to identify the message in the future (e.g. service notifications).
	 * @param destination the destination of the message.
	 * @param annotation semantic information provided by the application 
	 * to be used by communication protocols (e.g. semantic reliability).
	 * @throws IOException 
	 */
	public void send(Message msg, Service service, Object cookie, SocketAddress destination, 
			Annotation... annotation) throws IOException, UnsupportedServiceException;

}
