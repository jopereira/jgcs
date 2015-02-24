/*
 * IP Multicast implementation of JGCS - Group Communication Service.
 * Copyright (C) 2006,2013 José Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 *
 * See COPYING for licensing details.
 */
	  
package net.sf.jgcs.ip; 

import java.net.*;

import net.sf.jgcs.GroupConfiguration;

/**
 * Group identifier for IP multicast. A group is identified by a multicast
 * address and a port. 
 */
public class IpGroup implements GroupConfiguration {
	
	private static final long serialVersionUID = 2L;
	
	private InetSocketAddress address;

	/**
	 * Build an uninitialized group identifier.
	 */
	public IpGroup() {
	}
	
	/**
	 * Build a group address from a string in format "a.a.a.a:p".
	 * @param group group address and port
	 * @throws UnknownHostException
	 */
	public IpGroup(String group) throws UnknownHostException {
		String[] peer=group.split(":");
		address = new InetSocketAddress(peer[0], Integer.parseInt(peer[1]));
	}
		
	/**
	 * Set group's socket address.
	 * @param address the group's socket address
	 */
	public void setAddress(InetSocketAddress address) {
		this.address = address;
	}
	
	/**
	 * Get group's socket address.
	 * @return the group's socket address
	 */
	public InetSocketAddress getAddress() {
		return address;
	}
}

