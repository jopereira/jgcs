/*
 * Corosync/CPG implementation of JGCS - Group Communication Service.
 * Copyright (C) 2013 José Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs.corosync.jni;

import net.sf.jgcs.GroupException;

public class CorosyncException extends GroupException {
	private static final long serialVersionUID = 8111489446009578137L;
	
	private int error;

	private CorosyncException(int error, String string) {
		super(string);
		this.error = error;
	}	
	
	public int getError() {
		return error;
	}
	
	/*
	 * Error codes copied from include/corosync/corotypes.h.
	 * Copyright (c) 2008 Allied Telesis Labs.
	 * Copyright (c) 2012 Red Hat, Inc.
	 * See original file.
	 */
	public static final int CS_OK = 1;
	public static final int LIBRARY = 2;
	public static final int VERSION = 3;
	public static final int INIT = 4;
	public static final int TIMEOUT = 5;
	public static final int TRY_AGAIN = 6;
	public static final int INVALID_PARAM = 7;
	public static final int NO_MEMORY = 8;
	public static final int BAD_HANDLE = 9;
	public static final int BUSY = 10;
	public static final int ACCESS = 11;
	public static final int NOT_EXIST = 12;
	public static final int NAME_TOO_LONG = 13;
	public static final int EXIST = 14;
	public static final int NO_SPACE = 15;
	public static final int INTERRUPT = 16;
	public static final int NAME_NOT_FOUND = 17;
	public static final int NO_RESOURCES = 18;
	public static final int NOT_SUPPORTED = 19;
	public static final int BAD_OPERATION = 20;
	public static final int FAILED_OPERATION = 21;
	public static final int MESSAGE_ERROR = 22;
	public static final int QUEUE_FULL = 23;
	public static final int QUEUE_NOT_AVAILABLE = 24;
	public static final int BAD_FLAGS = 25;
	public static final int TOO_BIG = 26;
	public static final int NO_SECTIONS = 27;
	public static final int CONTEXT_NOT_FOUND = 28;
	public static final int TOO_MANY_GROUPS = 30;
	public static final int SECURITY = 100;
}	
