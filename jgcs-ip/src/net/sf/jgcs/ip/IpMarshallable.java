package net.sf.jgcs.ip;

import net.sf.jgcs.JGCSException;
import net.sf.jgcs.membership.MembershipID;
import net.sf.jgcs.utils.AbstractMarshallableSocketAddress;

public class IpMarshallable extends AbstractMarshallableSocketAddress {

	public IpMarshallable() {
		super();
	}

	public MembershipID getMembershipID(byte[] buffer) throws JGCSException {
		throw new JGCSException("Operation not supported by the IP implementation.");
	}

	public byte[] getBytes(MembershipID id) throws JGCSException {
		throw new JGCSException("Operation not supported by the IP implementation.");
	}

}
