/*
 * Corosync/CPG implementation of JGCS - Group Communication Service.
 * Copyright (C) 2013 José Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs.corosync;

import net.sf.jgcs.GroupConfiguration;

/**
 * Group identifier for Corosync. A group is identified by a string.
 */
public class CPGGroup implements GroupConfiguration {

	private static final long serialVersionUID = 2L;
	
	private String group;
	
	/**
	 * Build a group identifier from a string.
	 * @param name the name
	 */
	public CPGGroup(String group) {
		this.group=group;
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
		return group.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof CPGGroup && ((CPGGroup)other).group.equals(group);
	}
	
	@Override
	public String toString() {
		return group;
	}
}