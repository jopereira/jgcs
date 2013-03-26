
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import net.sf.jgcs.JGCSException;
import net.sf.jgcs.membership.MembershipID;

import org.jgroups.ViewId;

public class JGroupsMembershipID implements MembershipID {

	private static final long serialVersionUID = 2L;
	
	private ViewId id;

	public JGroupsMembershipID() {
		id = new ViewId();
	}

	public JGroupsMembershipID(ViewId id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JGroupsMembershipID other = (JGroupsMembershipID) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public byte[] getBytes() throws JGCSException{
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(byteStream);
			id.writeTo(out);
			out.close();
		} catch (Exception e) {
			throw new JGCSException("Could not write to output stream", e);
		}
		return byteStream.toByteArray();
	}

	public void fromBytes(byte[] bytes) throws JGCSException{
		ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
		DataInputStream in = null;
		try {
			in = new DataInputStream(byteStream);
			id.readFrom(in);
			in.close();
		} catch (Exception e) {
			throw new JGCSException("Could not read from input stream", e);
		}
	}
}
