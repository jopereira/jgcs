package net.sf.jgcs.neem;

import net.sf.jgcs.JGCSException;
import net.sf.jgcs.membership.MembershipID;
import net.sf.jgcs.utils.AbstractMarshallableSocketAddress;

public class NeEMMarshallable extends AbstractMarshallableSocketAddress {

	public NeEMMarshallable() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MembershipID getMembershipID(byte[] buffer) throws JGCSException {
		throw new JGCSException("Operation not supported by the NeEM implementation.");
	}

	public byte[] getBytes(MembershipID id) throws JGCSException {
		throw new JGCSException("Operation not supported by the NeEM implementation.");
	}

}
