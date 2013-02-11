
/*
 * NeEM implementation of JGCS - Group Communication Service.
 * Copyright (C) 2006 Jose' Orlando Pereira, Universidade do Minho
 *
 * jop@di.uminho.pt - http://gsd.di.uminho.pt/~jop
 * jgcs@lasige.di.fc.ul.pt
 *
 * Departamento de Informatica, Universidade do Minho
 * Campus de Gualtar, 4710-057 Braga, Portugal
 *
 * See COPYING for licensing details.
 */
	  
package net.sf.jgcs.neem;

import java.net.*;

import net.sf.jgcs.GroupConfiguration;

public class NeEMGroup implements GroupConfiguration {
	private InetSocketAddress localAddress;

	private InetSocketAddress[] peers;

	private static InetSocketAddress parseAddress(String group)
			throws UnknownHostException {
		String[] peer = group.split(":");
		int port = Integer.parseInt(peer[1]);
		InetAddress peerAddress = InetAddress.getByName(peer[0]);
		return new InetSocketAddress(peerAddress, port);
	}

	public NeEMGroup(String group) throws UnknownHostException {
		String[] addresses = group.split(",");
		localAddress = parseAddress(addresses[0]);
		peers = new InetSocketAddress[addresses.length - 1];
		for (int i = 1; i < addresses.length; i++)
			peers[i - 1] = parseAddress(addresses[i]);
	}

	 public NeEMGroup(InetSocketAddress local, InetSocketAddress[] peers) {
		this.localAddress = local;
		this.peers = peers;
	}

	public InetSocketAddress getLocalAddress() {
		return localAddress;
	}

	public InetSocketAddress[] getPeers() {
		return peers;
	}
}
