
/*
 * NeEM implementation of JGCS - Group Communication Service.
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
	  
package net.sf.jgcs.neem;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

import net.sf.jgcs.Message;


class NeEMMessage implements Message {

	private static final long serialVersionUID = 2L;
	
	private byte[] payload;
	private SocketAddress sender;

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

	public SocketAddress getSenderAddress() {
		return sender;
	}
}
