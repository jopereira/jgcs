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

package net.sf.jgcs.corosync.jni;

import net.sf.jgcs.corosync.CPGAddress;

public class ClosedProcessGroup {
	private long handle;
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

	private native void _initialize() throws CorosyncException;
	private native void _finalize() throws CorosyncException;
	
	public native void join(String group) throws CorosyncException;
	public native void leave(String group) throws CorosyncException;
	
	public static final int CS_DISPATCH_ONE = 1;
	public static final int CS_DISPATCH_ALL = 2;
	public static final int CS_DISPATCH_BLOCKING = 3;
	public static final int CS_DISPATCH_ONE_NONBLOCKING = 4;
	
	public native synchronized void dispatch(int mode) throws CorosyncException;

	public static final int CPG_TYPE_UNORDERED = 0;
	public static final int CPG_TYPE_FIFO = 1;
	public static final int CPG_TYPE_AGREED = 2;
	public static final int CPG_TYPE_SAFE = 3;
	
	public native void multicast(int guarantee, byte[] msg) throws CorosyncException;
	
	public native int getLocalNodeId() throws CorosyncException;
	public native int getProcessId();
	
	public void close() throws CorosyncException {
		_finalize();
	}
	
	public static final int CPG_REASON_JOIN = 1;
	public static final int CPG_REASON_LEAVE = 2;
	public static final int CPG_REASON_NODEDOWN = 3;
	public static final int CPG_REASON_NODEUP = 4;
	public static final int CPG_REASON_PROCDOWN = 5;
	
	public static interface Callbacks {
		public void deliver(String group, int nodeid, int pid, byte[] msg);
		public void configurationChange(String group, CPGAddress[] members, CPGAddress[] left, int[] lreason, CPGAddress[] joined, int[] jreason);
		public void ringChange(int nodeid, long seq, int[] nodes);
	};
	
	static {
		System.loadLibrary("cpgjgcs");
	};
}