
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
 * This class defines a Message.
 * Messages exchanged using the underlying toolkit must implement this interface.
 * Instances of this interface must be retrieved from the 
 * {@link DataSession DataSession}.
 * 
 * @composed 1 Defines 1 byte[]
 * @composed 1 SenderAddress 1 java.net.SocketAddress
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public interface Message {

	/**
	 * Sets the payload for the message.
	 * 
	 * @param buffer The payload to be stored in the message.
	 */
	public void setPayload(byte[] buffer);
	
	/**
	 * Gets the payload from the message.
	 * 
	 * @return the payload from the message.
	 */
	public byte[] getPayload();
	
	/**
	 * Gets the sender address.
	 * @return the sender address
	 */
	public SocketAddress getSenderAddress();
	
	/**
	 * Sets the sender address.
	 * @param sender the sender address.
	 */
	public void setSenderAddress(SocketAddress sender);
}
