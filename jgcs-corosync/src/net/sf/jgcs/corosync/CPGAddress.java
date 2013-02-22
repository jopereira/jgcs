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

import java.net.SocketAddress;

public class CPGAddress extends SocketAddress {
	private int nodeid, pid;

	CPGAddress(int nodeid, int pid) {
		this.nodeid = nodeid;
		this.pid = pid;
	}

	public int getNodeId() {
		return nodeid;
	}

	public int getProcessId() {
		return pid;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + nodeid;
		result = prime * result + pid;
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
		CPGAddress other = (CPGAddress) obj;
		if (nodeid != other.nodeid)
			return false;
		if (pid != other.pid)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "process-"+pid+"@"+nodeid;
	}
}
