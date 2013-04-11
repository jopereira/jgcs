/*
 * NeEM implementation of JGCS - Group Communication Service.
 * Copyright (C) 2006,2013 José Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 *
 * See COPYING for licensing details.
 */
	  
package net.sf.jgcs.neem;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

import net.sf.jgcs.Message;


class NeEMMessage implements Message {

	private static final long serialVersionUID = 2L;
	
	private byte[] payload;

	NeEMMessage() {}
	
	NeEMMessage(ByteBuffer buf) {
		payload=new byte[buf.remaining()];
		buf.get(payload);
	}

	public void setPayload(byte[] buffer) {
		payload=buffer;
	}

	public byte[] getPayload() {
		return payload;
	}

	/**
	 * NeEM does not provide a sender address.
	 */
	public SocketAddress getSenderAddress() {
		return null;
	}
}
