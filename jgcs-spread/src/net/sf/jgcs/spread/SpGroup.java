/*
 * Spread implementation of JGCS - Group Communication Service.
 * Copyright (C) 2006,2013 Jose' Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 * 
 * See COPYING for licensing details.
 */

package net.sf.jgcs.spread;

import java.net.SocketAddress;

import net.sf.jgcs.GroupConfiguration;

/**
 * Group identifier in Spread. A group is identified by a string.
 */
public class SpGroup extends SocketAddress implements GroupConfiguration {

	private static final long serialVersionUID = 2L;
	
	private String group;
	
	/**
	 * Build a group identifier. The name should be set before being used.
	 */
	public SpGroup() {
		super();
	}

	/**
	 * Build a group identifier from a string.
	 * @param name the name
	 */
	public SpGroup(String name) {
		this.group = name;
	}

	/**
	 * Set group name.
	 * @param name the name
	 */
	public void setGroup(String name) {
		this.group = name;
	}
	
	/**
	 * Get group name.
	 * @return the name
	 */
	public String getGroup() {
		return group;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((group == null) ? 0 : group.hashCode());
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
		SpGroup other = (SpGroup) obj;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		return true;
	}
}
