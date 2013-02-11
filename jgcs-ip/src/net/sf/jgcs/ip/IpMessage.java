
/*
 * IP Multicast implementation of JGCS - Group Communication Service.
 * Copyright (C) 2006 Jose' Orlando Pereira, Universidade do Minho
 *
 * jop@di.uminho.pt - http://gsd.di.uminho.pt/~jop
 * jgcs@lasige.di.fc.ul.pt
 *
 * Departamento de Informatica, Universidade do Minho
 * Campus de Gualtar, 4710-057 Braga, Portugal
 *
 * See COPYING for licensing details.
 */
	  
package net.sf.jgcs.ip;

import java.net.*;

import net.sf.jgcs.Message;

class IpMessage implements Message {

	private byte[] payload;
	private SocketAddress sender;

	IpMessage() {}
	
	IpMessage(DatagramPacket dgram) {
		byte[] buf=dgram.getData();
		payload=new byte[dgram.getLength()];
		System.arraycopy(buf,0,payload,0,payload.length);
		sender = dgram.getSocketAddress();
	}
	
	public void setPayload(byte[] payload) {
		this.payload=payload;
	}

	public byte[] getPayload() {
		return payload;
	}

	public SocketAddress getSenderAddress() {
		return sender;
	}

	public void setSenderAddress(SocketAddress sender) {
		this.sender = sender;
	}
}
