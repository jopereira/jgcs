/*
 * Corosync/CPG implementation of JGCS - Group Communication Service.
 * Copyright (C) 2013 José Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 *
 * See COPYING for licensing details.
 */
  
package net.sf.jgcs.corosync;

import java.net.SocketAddress;

import net.sf.jgcs.Message;
import net.sf.jgcs.corosync.jni.ClosedProcessGroup.Address;

class CPGMessage implements Message {

	private static final long serialVersionUID = 2L;
	
	private byte[] payload;
	private SocketAddress sender;
	
	CPGMessage() {
	}
	
	CPGMessage(byte[] data, Address sender) {
		this.payload = data;
		this.sender =sender;
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
