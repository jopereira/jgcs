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

import net.sf.jgcs.GroupConfiguration;

/**
 * Group identifier in JGroups. A group is identified by a string.
 */
public class JGroupsGroup implements GroupConfiguration {

	private static final long serialVersionUID = 2L;
	
	private String groupName;
	
	/**
	 * Build a group identifier. The name should be set before being used.
	 */
	public JGroupsGroup() {
		super();
	}

	/**
	 * Build a group identifier from a string.
	 * @param name the name
	 */
	public JGroupsGroup(String name) {
		this.groupName = name;
	}

	/**
	 * Set group name.
	 * @param name the name
	 */
	public void setGroupName(String name) {
		this.groupName = name;
	}
	
	/**
	 * Get group name.
	 * @return the name
	 */
	public String getGroupName() {
		return groupName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((groupName == null) ? 0 : groupName.hashCode());
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
		JGroupsGroup other = (JGroupsGroup) obj;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		return true;
	}

}
