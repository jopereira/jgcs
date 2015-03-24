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

import org.jgroups.Message;

import net.sf.jgcs.Service;

/**
 * Service configuration. JGroups services are configured by setting
 * flags that omit certain delivery guarantees. Available flags are
 * available in <a href="http://www.jgroups.org/javadoc/org/jgroups/Message.Flag.html">
 * JGroups documentation</a>.
 */
public class JGroupsService implements Service {

	private static final long serialVersionUID = 2L;

	private short flags;

	/**
	 * Create the default service configuration, with no flags set.
	 */
	public JGroupsService() {
	}

	/**
	 * Create a configuration from JGroups message flags.
	 * @param flags message flags
	 */
	public JGroupsService(Message.Flag... flags) {
		this.setFlags(flags);
	}

	/**
	 * Create a configuration from JGroups message flag names. Names
	 * for multiple flags can be specified, sepated by the | (pipe)
	 * character.
	 * @param flagNames message flag names
	 */
	public JGroupsService(String flagNames) {
		this.setFlags(flagNames);
	}

	/**
	 * Create a configuration from JGroups message flag values.
	 * @param flags flag values
	 */
	public JGroupsService(short flags) {
		this.setFlags(flags);
	}
	
	/**
	 * Set additional flags from JGroups message flags. Set the flags
	 * first to 0 to reset them all.
	 * @param flags message flags
	 */
	public void setFlags(Message.Flag... flags) {
		for(Message.Flag flag: flags)
			this.flags |= flag.value();
	}
	
	/**
	 * Set additional flags from JGroups message flag names. Set the flags
	 * first to 0 to reset them all.
 	 * @param flagNames message flag names
	 */
	public void setFlags(String flagNames) {
		String[] flags = flagNames.split("[|]");
		for(String flag: flags)
			this.flags |= Message.Flag.valueOf(flag).value();
	}

	/**
	 * Set flags to JGroups message flag names. This resets all flags
	 * that are not explicitly specified.
 	 * @param flags message flag values
	 */
	public void setFlags(short flags) {
		this.flags = flags;
	}
	
	/**
	 * Get flag values.
	 * @return flag values
	 */
	public short getFlags() {
		return flags;
	}

	@Override
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
