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
