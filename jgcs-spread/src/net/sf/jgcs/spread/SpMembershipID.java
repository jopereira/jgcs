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
	private int[] id;

	public SpMembershipID(int[] group_id) {
		this.id = group_id;
	}

	@Override
	public int compareTo(MembershipID o) {
		if (!(o instanceof SpMembershipID))
			return 0;
		SpMembershipID other = (SpMembershipID)o;
		if (id[0]!=other.id[0] || id[1]!=other.id[1])
			return 0;
		return Integer.compare(id[2], other.id[2]);
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
