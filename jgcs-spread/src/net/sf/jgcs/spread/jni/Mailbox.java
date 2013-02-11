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
 * JNI binding of native APIs. This closely matches the C API
 * of Spread. Temporary objects are used for out parameters.
 */
/*
 * WARNING: Do not change field or method names! These 
 * are referenced by native code in csrc/.
 */
public abstract class Mailbox {
	@SuppressWarnings("unused")
	private int mailbox;
	private String private_group;

	public abstract int C_connect(String spread_name, String private_name,
			boolean priority, boolean group_membership);

	public abstract int C_disconnect();

	public abstract int C_join(String group);

	public abstract int C_leave(String group);

	public abstract int C_flush(String group);
	
	/**
	 * Information used to specify the destination and the
	 * service-type when sending messages. See the documentation
	 * of SP_multigroup_multicast.
	 */
	public static class MulticastArgs {
		public MulticastArgs() {}

		public MulticastArgs(int service_type, String group, String[] groups, short mess_type) {
			this.service_type=service_type;
			this.group_name=group;
			this.groups=groups;
			this.mess_type=mess_type;
		}

		public int service_type;
		public String group_name;
		public String[] groups;
		public short mess_type;
	}

	public abstract int C_multicast(MulticastArgs info, ByteBuffer mess);

	public abstract int C_subgroupcast(MulticastArgs info, ByteBuffer mess);

	/**
	 * Information returned when receiving a message about the sender
	 * and the service-type.
	 */
	public static class ReceiveArgs extends MulticastArgs {
		public ReceiveArgs() {}

		public int max_groups=10;
		public String sender;
		public int endian_mismatch;
	}
	
	public abstract int C_receive(ReceiveArgs info, ByteBuffer mess);

	/**
	 * Additional information contained in membership messages.
	 */
	public static class ViewInfo {
		public String group_id;
		public String[] vs_set;
	}

	public abstract int C_parseView(ViewInfo info, MulticastArgs rinfo, ByteBuffer mess);
	
	/**
	 * Returns the private group name of this connection. This is initialized
	 * when the mailbox is connected.
	 */
	public String getPrivateGroup() {
		return private_group;
	}

	/**
	 * Application code should call disconnect directly and not rely on this.
	 */
	protected void finalize() throws Throwable {
		C_disconnect();
	}
	
	// Calling C_init is required to initialize the native code.
	static {
		System.loadLibrary("spjgcs");
		C_init();
	};
	
	private static native int C_init();
}
