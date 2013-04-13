/*
 * Corosync/CPG implementation of JGCS - Group Communication Service.
 * Copyright (C) 2013 José Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs.corosync.jni;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;

public class ClosedProcessGroup implements Closeable {
	private long handle;
	private boolean busy;
	private Callbacks callbacks;
	
	public ClosedProcessGroup(Callbacks callbacks, int flags) throws CorosyncException {
		this.callbacks = callbacks;
		_initialize();
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		_finalize();
	}
	
	@Override
	public void close() throws IOException {
		_finalize();
	}

	private native void _initialize() throws CorosyncException;
	private native void _finalize() throws CorosyncException;
	
	public native void join(String group) throws CorosyncException;
	public native void leave(String group) throws CorosyncException;
	
	public static final int CS_DISPATCH_ONE = 1;
	public static final int CS_DISPATCH_ALL = 2;
	public static final int CS_DISPATCH_BLOCKING = 3;
	public static final int CS_DISPATCH_ONE_NONBLOCKING = 4;
	
	/**
	 * This method will throw an exception if re-entered.
	 */
	public native void dispatch(int mode) throws CorosyncException;

	public static final int CPG_TYPE_UNORDERED = 0;
	public static final int CPG_TYPE_FIFO = 1;
	public static final int CPG_TYPE_AGREED = 2;
	public static final int CPG_TYPE_SAFE = 3;
	
	public native void multicast(int guarantee, byte[] msg) throws CorosyncException;
	
	public native Address getLocalAddress() throws CorosyncException;
	
	public static final int CPG_REASON_JOIN = 1;
	public static final int CPG_REASON_LEAVE = 2;
	public static final int CPG_REASON_NODEDOWN = 3;
	public static final int CPG_REASON_NODEUP = 4;
	public static final int CPG_REASON_PROCDOWN = 5;
	
	public static class Address extends SocketAddress {

		private static final long serialVersionUID = 2L;
		
		private int nodeid, pid;

		public Address(int nodeid, int pid) {
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
			Address other = (Address) obj;
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
		
	public static interface Callbacks {
		public void deliver(String group, Address address, byte[] msg);
		public void configurationChange(String group, Address[] members, Address[] left, int[] lreason, Address[] joined, int[] jreason);
		public void ringChange(int nodeid, long seq, int[] nodes);
	};
	
	static {
		System.loadLibrary("cpgjgcs");
	};
}