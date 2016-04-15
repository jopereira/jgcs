/*
 * JGroups implementation of JGCS - Group Communication Service
 * Copyright (C) 2006 Nuno Carvalho, Universidade de Lisboa
 * Copyright (C) 2013 José Orlando Pereira
 * 
 * http://github.com/jopereira/jgcs
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
 
package net.sf.jgcs.jgroups;

import java.io.DataInput;
import java.io.DataOutput;
import java.net.SocketAddress;

import org.jgroups.Address;
import org.jgroups.util.Util;

/**
 * Wraps a JGroups message. It provides a public construtor and implements
 * the Externalizable interface such that it can be serialized. Messages
 * should be created only using the corresponding DataSession.
 */
public class JGroupsMessage extends org.jgroups.Message implements net.sf.jgcs.Message {
	
	private JGroupsSocketAddress sender;

	public JGroupsMessage() {
		super();
	}

	JGroupsMessage(byte[] buffer, JGroupsSocketAddress sender) {
		super();
		this.sender = sender;
		this.setBuffer(buffer);
	}

	@Override
	public void setPayload(byte[] buffer) {
		this.setBuffer(buffer);
	}

	@Override
	public byte[] getPayload() {
		return this.getBuffer();
	}

	@Override
	public SocketAddress getSenderAddress() {
		return this.sender;
	}

 	@Override
    public void readFrom(DataInput in) throws Exception {
		super.readFrom(in);
		Address addr = Util.readAddress(in);
		if (addr != null)
			sender = new JGroupsSocketAddress(addr);
	}

	@Override
    public void writeTo(DataOutput out) throws Exception {
		super.writeTo(out);
		if (sender != null)
			Util.writeAddress(sender.getAddress(), out);
		else
			Util.writeAddress(null, out);
	}	
}
