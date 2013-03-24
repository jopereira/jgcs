package net.sf.jgcs.spread;

import java.net.SocketAddress;

import net.sf.jgcs.JGCSException;
import net.sf.jgcs.membership.MembershipID;
import net.sf.jgcs.utils.Marshallable;

public class SpMarshallable implements Marshallable {

	public SpMarshallable() {
		super();
	}

	public MembershipID getMembershipID(byte[] buffer) throws JGCSException {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] getBytes(MembershipID id) throws JGCSException {
		// TODO Auto-generated method stub
		return null;
	}

	public SocketAddress getSocketAddress(byte[] buffer) throws JGCSException {
		return new SpGroup(new String(buffer));
	}

	public byte[] getBytes(SocketAddress addr) throws JGCSException {
		if ( ! (addr instanceof SpGroup))
			throw new JGCSException("Address of type "+addr.getClass().getName()+" is not supported.");
		return ((SpGroup) addr).getGroup().getBytes();
	}

}
