package net.sf.jgcs.utils;

import java.net.SocketAddress;

import net.sf.jgcs.JGCSException;
import net.sf.jgcs.membership.MembershipID;

public interface Marshallable {
	
	public MembershipID getMembershipID(byte[] buffer) throws JGCSException;
	public byte[] getBytes(MembershipID id) throws JGCSException;
	public SocketAddress getSocketAddress(byte[] buffer) throws JGCSException;
	public byte[] getBytes(SocketAddress addr) throws JGCSException;

}
