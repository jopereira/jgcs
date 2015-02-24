/*
 * Spread implementation of JGCS - Group Communication Service.
 * Copyright (C) 2006,2013 Jose' Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 * 
 * See COPYING for licensing details.
 */

package net.sf.jgcs.spread;

import java.util.Arrays;

import net.sf.jgcs.MembershipID;

class SpMembershipID implements MembershipID {

	private static final long serialVersionUID = 2L;
	
	private int[] id;

	SpMembershipID(int[] group_id) {
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

	@Override
	public String toString() {
		return id[0]+"-"+id[1]+"-"+id[2];
	}
}
