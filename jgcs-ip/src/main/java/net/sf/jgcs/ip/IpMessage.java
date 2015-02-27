/*
 * IP Multicast implementation of JGCS - Group Communication Service.
 * Copyright (C) 2006,2013 José Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 *
 * See COPYING for licensing details.
 */
	  
package net.sf.jgcs.ip;

import java.net.*;

import net.sf.jgcs.Message;

class IpMessage implements Message {

	private static final long serialVersionUID = 2L;
	
	private byte[] payload;
	private SocketAddress sender;

	IpMessage() {}
	
	IpMessage(DatagramPacket dgram) {
		byte[] buf=dgram.getData();
		payload=new byte[dgram.getLength()];
		System.arraycopy(buf,0,payload,0,payload.length);
		sender = dgram.getSocketAddress();
	}
	
	@Override
	public void setPayload(byte[] payload) {
		this.payload=payload;
	}

	@Override
	public byte[] getPayload() {
		return payload;
	}

	@Override
	public SocketAddress getSenderAddress() {
		return sender;
	}
}
