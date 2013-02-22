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
import net.sf.jgcs.UnsupportedServiceException;
import net.sf.jgcs.corosync.jni.ClosedProcessGroup;

public class CPGService implements Service {
	
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
			guarantee = ClosedProcessGroup.CPG_TYPE_UNORDERED;
		else if (prop.equals("agreed"))
			guarantee = ClosedProcessGroup.CPG_TYPE_UNORDERED;
		else if (prop.equals("safe"))
			guarantee = ClosedProcessGroup.CPG_TYPE_UNORDERED;
	}

	public int getGuarantee() {
		return guarantee;
	}

	public int compare(Service service) throws UnsupportedServiceException {
		CPGService other;
		try {
			other=(CPGService)service;
		} catch(ClassCastException e) {
			throw new UnsupportedServiceException("unsupported service "+service);
		}
		return guarantee-other.guarantee;
	}
}
