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

import java.util.Arrays;

import net.sf.jgcs.membership.MembershipID;

public class SpMembershipID implements MembershipID {

	private static final long serialVersionUID = 2L;
	
	private int[] id;

	public SpMembershipID(int[] group_id) {
		this.id = group_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(id);
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
		SpMembershipID other = (SpMembershipID) obj;
		if (!Arrays.equals(id, other.id))
			return false;
		return true;
	}

	public String toString() {
		return id[0]+"-"+id[1]+"-"+id[2];
	}
}
