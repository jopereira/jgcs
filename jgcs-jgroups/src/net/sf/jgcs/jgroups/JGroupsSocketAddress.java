package net.sf.jgcs.jgroups;

import java.net.SocketAddress;

import org.jgroups.Address;

public class JGroupsSocketAddress extends SocketAddress {

	private static final long serialVersionUID = 2L;
	
	private Address id;

	public JGroupsSocketAddress(Address address) {
		this.id = address;
	}

	public String toString() {
		return id.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		JGroupsSocketAddress other = (JGroupsSocketAddress) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Address getAddress() {
		return id;
	}
}
