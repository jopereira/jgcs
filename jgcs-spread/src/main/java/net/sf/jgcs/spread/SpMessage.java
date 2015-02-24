/*
 * Spread implementation of JGCS - Group Communication Service.
 * Copyright (C) 2006,2013 Jose' Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 * 
 * See COPYING for licensing details.
 */

package net.sf.jgcs.spread;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

import net.sf.jgcs.Message;

class SpMessage implements Message {

	private static final long serialVersionUID = 2L;
	
	private byte[] payload;
	private SocketAddress sender;
	
	SpMessage() {
	}
	
	SpMessage(ByteBuffer buf, SocketAddress sender) {
		payload=new byte[buf.remaining()];
		buf.get(payload);
		this.sender = sender;
	}

	@Override
	public void setPayload(byte[] buffer) {
		payload=buffer;
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
