/*
 * Spread implementation of JGCS - Group Communication Service.
 * Copyright (C) 2004,2006,2013 Jose' Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 * 
 * See COPYING for licensing details.
 */
	  
package net.sf.jgcs.spread.jni;

import java.nio.ByteBuffer;

/**
 * Loads the native binding to Spread. 
 */
public class SpMailbox extends Mailbox {
	public native int C_connect(String spread_name, String private_name,
		boolean priority, boolean group_membership);
	
	public native int C_disconnect();

	public native int C_join(String group);

	public native int C_leave(String group);

	public int C_flush(String group) {
		return 0;
	}
	
	public native int C_multicast(MulticastArgs info, ByteBuffer mess);

	public int C_subgroupcast(MulticastArgs info, ByteBuffer mess) {
		return C_multicast(info, mess);
	}

	public native int C_receive(ReceiveArgs info, ByteBuffer mess);

	public native int C_parseView(ViewInfo info, MulticastArgs rinfo, ByteBuffer mess);
};
