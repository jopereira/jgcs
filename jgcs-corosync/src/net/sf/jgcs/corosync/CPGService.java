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

import net.sf.jgcs.Service;
import net.sf.jgcs.corosync.jni.ClosedProcessGroup;

public class CPGService implements Service {

	private static final long serialVersionUID = 2L;

	private int guarantee;

	public CPGService(int guarantee) {
		this.guarantee=guarantee;
	}
	
	public CPGService(String prop) {
		setGuarantee(prop);
	}

	public void setGuarantee(String prop) {
		if (prop.equals("unordered"))
			guarantee = ClosedProcessGroup.CPG_TYPE_UNORDERED;
		else if (prop.equals("fifo"))
			guarantee = ClosedProcessGroup.CPG_TYPE_FIFO;
		else if (prop.equals("agreed"))
			guarantee = ClosedProcessGroup.CPG_TYPE_AGREED;
		else if (prop.equals("safe"))
			guarantee = ClosedProcessGroup.CPG_TYPE_SAFE;
	}

	public int getGuarantee() {
		return guarantee;
	}

	public boolean satisfies(Service service) {
		if (!(service instanceof CPGService))
			return false;
		return Integer.compare(guarantee, ((CPGService)service).guarantee) >= 0;
	}
}
