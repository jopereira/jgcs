/*
 * Corosync/CPG implementation of JGCS - Group Communication Service.
 * Copyright (C) 2013 Universidade do Minho
 *
 * jop@di.uminho.pt - http://www.di.uminho.pt/~jop
 *
 * Departamento de Informatica, Universidade do Minho
 * Campus de Gualtar, 4710-057 Braga, Portugal
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
	
	public int hashCode() {
		return group.hashCode();
	}
	
	public boolean equals(Object other) {
		return other instanceof CPGGroup && ((CPGGroup)other).group.equals(group);
	}
	
	public String toString() {
		return group;
	}
}