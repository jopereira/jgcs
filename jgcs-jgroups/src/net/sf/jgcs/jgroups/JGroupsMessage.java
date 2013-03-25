/*
 *
 * JGroups implementation of JGCS - Group Communication Service
 * Copyright (C) 2006 Nuno Carvalho, Universidade de Lisboa
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
 * 
 * Contact
 * 	Address:
 * 		LASIGE, Departamento de Informatica, Bloco C6
 * 		Faculdade de Ciencias, Universidade de Lisboa
 * 		Campo Grande, 1749-016 Lisboa
 * 		Portugal
 * 	Email:
 * 		jgcs@lasige.di.fc.ul.pt
 * 
 */
 
package net.sf.jgcs.jgroups;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.SocketAddress;

public class JGroupsMessage extends org.jgroups.Message implements net.sf.jgcs.Message, Externalizable {
	
	private SocketAddress sender;

	public JGroupsMessage() {
		super();
	}

	public void setPayload(byte[] buffer) {
		this.setBuffer(buffer);
	}

	public byte[] getPayload() {
		byte[] jgroupsBuffer = this.getBuffer();
		byte[] array = new byte[jgroupsBuffer.length];
		System.arraycopy(jgroupsBuffer,0,array,0,array.length);
		return array;
	}

	public SocketAddress getSenderAddress() {
		return this.sender;
	}

	public void setSenderAddress(SocketAddress sender) {
		this.sender = sender;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			super.readFrom(in);
		} catch (Exception e) {
			throw new IOException(e);
		}
		sender = (SocketAddress) in.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			super.writeTo(out);
		} catch (Exception e) {
			throw new IOException(e);
		}
		out.writeObject(sender);
	}	
}
