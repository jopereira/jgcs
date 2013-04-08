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

import org.jgroups.Message;

import net.sf.jgcs.Service;

public class JGroupsService implements Service {

	private static final long serialVersionUID = 2L;

	private short flags;

	public JGroupsService() {
	}

	public JGroupsService(Message.Flag... flags) {
		this.setFlags(flags);
	}

	public JGroupsService(String flagNames) {
		this.setFlags(flagNames);
	}

	public JGroupsService(short flags) {
		this.setFlags(flags);
	}
	
	public void setFlags(Message.Flag... flags) {
		for(Message.Flag flag: flags)
			this.flags |= flag.value();
	}
	
	public void setFlags(String flagNames) {
		String[] flags = flagNames.split("[|]");
		for(String flag: flags)
			this.flags |= Message.Flag.valueOf(flag).value();
	}

	public void setFlags(short flags) {
		this.flags = flags;
	}
	
	public short getFlags() {
		return flags;
	}

	public boolean satisfies(Service service) {
		if(! (service instanceof JGroupsService))
			return false;
		JGroupsService as = (JGroupsService) service;
		return (flags | as.flags) == as.flags;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + flags;
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
		JGroupsService other = (JGroupsService) obj;
		if (flags != other.flags)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return Message.flagsToString(flags);
	}
}
