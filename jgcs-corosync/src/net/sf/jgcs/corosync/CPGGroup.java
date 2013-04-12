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

public class CPGGroup implements GroupConfiguration {

	private static final long serialVersionUID = 2L;
	
	private String group;
	
	public CPGGroup(String group) {
		this.group=group;
	}
	
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