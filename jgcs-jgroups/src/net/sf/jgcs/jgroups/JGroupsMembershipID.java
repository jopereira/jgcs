
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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.sf.jgcs.JGCSException;
import net.sf.jgcs.membership.MembershipID;

import org.jgroups.Address;
import org.jgroups.ViewId;


public class JGroupsMembershipID extends ViewId implements MembershipID {

	private static final long serialVersionUID = -1585298721725820115L;

	public JGroupsMembershipID() {
		super();
	}

	public JGroupsMembershipID(Address coord_addr, long id) {
		super(coord_addr, id);
	}

	public JGroupsMembershipID(Address coord_addr) {
		super(coord_addr);
	}
	
	public byte[] getBytes() throws JGCSException{
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(byteStream);
			super.writeExternal(out);
			out.close();
		} catch (IOException e) {
			throw new JGCSException("Could not write to output stream", e);
		}
		return byteStream.toByteArray();
	}

	public void fromBytes(byte[] bytes) throws JGCSException{
		ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(byteStream);
			super.readExternal(in);
			in.close();
		} catch (IOException e) {
			throw new JGCSException("Could not read from input stream", e);
		} catch (ClassNotFoundException e) {
			throw new JGCSException("Could not read from input stream", e);
		}
	}

}
