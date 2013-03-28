
/*
 * IP Multicast implementation of JGCS - Group Communication Service.
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
	  
package net.sf.jgcs.ip; 

import java.net.*;

import net.sf.jgcs.GroupConfiguration;

public class IpGroup implements GroupConfiguration {
	
	private static final long serialVersionUID = 2L;
	
	private InetSocketAddress address;

	public IpGroup(String group) throws UnknownHostException {
		String[] peer=group.split(":");
		address = new InetSocketAddress(peer[0], Integer.parseInt(peer[1]));
	}
	public InetAddress getGroupAddress() {
		return address.getAddress();
	}
	public int getPort() {
		return address.getPort();
	}
	public InetSocketAddress getAddress() {
		return address;
	}
}

