
/*
 * Spread implementation of JGCS - Group Communication Service.
 * Copyright (C) 2006 Jose' Orlando Pereira, Universidade do Minho
 *
 * jop@di.uminho.pt - http://gsd.di.uminho.pt/~jop
 * jgcs@lasige.di.fc.ul.pt
 *
 * Departamento de Informatica, Universidade do Minho
 * Campus de Gualtar, 4710-057 Braga, Portugal
 *
 * See COPYING for licensing details.
 */
	  
package net.sf.jgcs.spread;

import java.net.SocketAddress;

import net.sf.jgcs.GroupConfiguration;


public class SpGroup extends SocketAddress implements GroupConfiguration {
	private static final long serialVersionUID = 1L;
	private String group;
	
	public SpGroup(String group) {
		this.group=group;
	}
	
	public String getGroup() {
		return group;
	}
	
	public int hashCode() {
		return group.hashCode();
	}
	
	public boolean equals(Object other) {
		return other instanceof SpGroup && ((SpGroup)other).group.equals(group);
	}
	
	public String toString() {
		return group;
	}
}
