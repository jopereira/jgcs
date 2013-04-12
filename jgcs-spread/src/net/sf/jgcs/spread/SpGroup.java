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


public class SpGroup extends SocketAddress implements GroupConfiguration {

	private static final long serialVersionUID = 2L;
	
	private String group;
	
	public SpGroup(String group) {
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
		return other instanceof SpGroup && ((SpGroup)other).group.equals(group);
	}
	
	@Override
	public String toString() {
		return group;
	}
}
