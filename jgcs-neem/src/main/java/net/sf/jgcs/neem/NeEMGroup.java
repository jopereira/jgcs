/*
 * NeEM implementation of JGCS - Group Communication Service.
 * Copyright (C) 2006,2013 José Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 *
 * See COPYING for licensing details.
 */
	  	  
package net.sf.jgcs.neem;

import java.net.*;

import net.sf.jgcs.GroupConfiguration;

/**
 * Group identifier in NeEM. A group is identified by a local address and
 * a set of contact addresses for the overlay network.  
 */
public class NeEMGroup implements GroupConfiguration {

	private static final long serialVersionUID = 2L;
	
	private InetSocketAddress localAddress;

	private InetSocketAddress[] peers;

	private static InetSocketAddress parseAddress(String group)
			throws UnknownHostException {
		String[] peer = group.split(":");
		int port = Integer.parseInt(peer[1]);
		InetAddress peerAddress = InetAddress.getByName(peer[0]);
		return new InetSocketAddress(peerAddress, port);
	}

	/**
	 * Build configuration from string. The expected format is , in format "a.a.a.a:p,a.a.a.a:p,...".
	 * The first address in the string is the local address and the others (if any) are
	 * potential contact points.
	 * 
	 * @param group local and contact addresses.
	 * @throws UnknownHostException
	 */
	public NeEMGroup(String group) throws UnknownHostException {
		String[] addresses = group.split(",");
		localAddress = parseAddress(addresses[0]);
		peers = new InetSocketAddress[addresses.length - 1];
		for (int i = 1; i < addresses.length; i++)
			peers[i - 1] = parseAddress(addresses[i]);
	}

	/**
	 * Build configuration from 
	 * 
	 * @param local local addresses.
	 * @param peers contact addresses.
	 * @throws UnknownHostException
	 */
	 public NeEMGroup(InetSocketAddress local, InetSocketAddress[] peers) {
		this.localAddress = local;
		this.peers = peers;
	}

	/**
	 * Get local addresss.
	 * @return local address.
	 */
	public InetSocketAddress getLocalAddress() {
		return localAddress;
	}

	/**
	 * Set local addresss.
	 * @param local address.
	 */
	public void setLocalAddress(InetSocketAddress local) {
		localAddress = local;
	}

	/**
	 * Get contact addresses.
	 * @return known contact addresses.
	 */
	public InetSocketAddress[] getPeers() {
		return peers;
	}

	/**
	 * Set contact addresses.
	 * @param peers known contact addresses.
	 */
	public void setPeers(InetSocketAddress[] peers) {
		this.peers = peers;
	}
}
