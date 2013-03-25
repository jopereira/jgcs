/*
 * Corosync/CPG implementation of JGCS - Group Communication Service.
 * Copyright (C) 2013 Universidade do Minho
 *
 * jop@di.uminho.pt - http://www.di.uminho.pt/~jop
 *
 * Departamento de Informatica, Universidade do Minho
 * Campus de Gualtar, 4710-057 Braga, Portugal
 *
 * See COPYING for licensing details.
 */
	  
package net.sf.jgcs.corosync;

import java.net.SocketAddress;

import net.sf.jgcs.Message;

class CPGMessage implements Message {

	private static final long serialVersionUID = 2L;
	
	private byte[] payload;
	private SocketAddress sender;
	
	CPGMessage() {
	}
	
	CPGMessage(byte[] data, CPGAddress sender) {
		this.payload = data;
		this.sender =sender;
	}

	public void setPayload(byte[] buffer) {
		payload=buffer;
	}

	public byte[] getPayload() {
		return payload;
	}

	public SocketAddress getSenderAddress() {
		return sender;
	}
}
