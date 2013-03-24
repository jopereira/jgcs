/*
 * Spread implementation of JGCS - Group Communication Service.
 * Copyright (C) 2004,2006 Jose' Orlando Pereira, Universidade do Minho
 *
 * jop@di.uminho.pt - http://gsd.di.uminho.pt/~jop
 * jgcs@lasige.di.fc.ul.pt
 *
 * Departamento de Informatica, Universidade do Minho
 * Campus de Gualtar, 4710-057 Braga, Portugal
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs.spread.jni;

import java.nio.ByteBuffer;

/**
 * Loads the native binding to Spread. 
 */
public class FlMailbox extends Mailbox {
	public native int C_connect(String spread_name, String private_name,
		boolean priority, boolean group_membership);

	public native int C_disconnect();
	
	public native int C_join(String group);
	
	public native int C_leave(String group);
	
	public native int C_flush(String group);
	
	public native int C_multicast(MulticastArgs info, ByteBuffer mess);
	
	public native int C_subgroupcast(MulticastArgs info, ByteBuffer mess);
	
	public native int C_receive(ReceiveArgs info, ByteBuffer mess);
	
	public native int C_parseView(ViewInfo info, MulticastArgs rinfo, ByteBuffer mess);
};
