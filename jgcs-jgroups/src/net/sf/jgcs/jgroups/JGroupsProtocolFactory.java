
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

import net.sf.jgcs.JGCSException;
import net.sf.jgcs.Protocol;
import net.sf.jgcs.ProtocolFactory;

public class JGroupsProtocolFactory implements ProtocolFactory {

	private static final long serialVersionUID = 2L;

	public JGroupsProtocolFactory() {
		super();
	}

	/**
	 * @deprecated
	 * @param nil
	 */
	public JGroupsProtocolFactory(String nil) {
		super();
	}

	public Protocol createProtocol() throws JGCSException{
		return new JGroupsProtocol();
	}

}
