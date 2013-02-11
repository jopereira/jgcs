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

import net.sf.jgcs.membership.MembershipID;

public class SpMembershipID implements MembershipID {
	private String id;

	public SpMembershipID(String id) {
		this.id = id;
	}
	
	public int compareTo(Object o) {
		return 0;
	}

	public String toString() {
		return id;
	}
}
