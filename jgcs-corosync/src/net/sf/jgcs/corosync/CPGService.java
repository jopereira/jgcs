/*
 * Corosync/CPG implementation of JGCS - Group Communication Service.
 * Copyright (C) 2013 José Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs.corosync;

import net.sf.jgcs.Service;
import net.sf.jgcs.corosync.jni.ClosedProcessGroup;

/**
 * Service configuration. JGroups services are configured by selecting
 * one delivery guarantee. See {@link ClosedProcessGroup} for possible
 * guarantees.
 */
public class CPGService implements Service {

	private static final long serialVersionUID = 2L;

	private int guarantee;

	/**
	 * Create the default service configuration (safe).
	 */
	public CPGService() {
		this.guarantee=ClosedProcessGroup.CPG_TYPE_SAFE;
	}
	
	/**
	 * Create a service configuration using constants in {@link ClosedProcessGroup}. 
	 */
	public CPGService(int guarantee) {
		this.guarantee=guarantee;
	}
	
	/**
	 * Create a service configuration using a string identifier: unordered,
	 * fifo, agreed, or safe.
	 */
	public CPGService(String prop) {
		setGuarantee(prop);
	}

	/**
	 * Set guarantee using a string identifier: unordered, fifo, agreed, or safe.
	 */
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

	/**
	 * Get currently set guarantee.
	 * @return guarantee as integer constant.
	 */
	public int getGuarantee() {
		return guarantee;
	}

	@Override
	public boolean satisfies(Service service) {
		if (!(service instanceof CPGService))
			return false;
		return Integer.compare(guarantee, ((CPGService)service).guarantee) >= 0;
	}
}
