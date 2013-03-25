
/*
 * Spread implementation of JGCS - Group Communication Service.
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
	  
package net.sf.jgcs.spread;

import net.sf.jgcs.Service;
import net.sf.jgcs.UnsupportedServiceException;

public class SpService implements Service {

	private static final long serialVersionUID = 2L;
	
	// Service types for regular messages
	public static final int REGULAR_MESS=0x0000003f;

	public static final int SAFE_MESS=0x00000020;
	public static final int AGREED_MESS=0x00000010;
	public static final int CAUSAL_MESS=0x00000008;
	public static final int FIFO_MESS=0x00000004;
	public static final int RELIABLE_MESS=0x00000002;
	public static final int UNRELIABLE_MESS=0x00000001;

	// Service types for membership messages
	public static final int MEMBERSHIP_MESS=0x00003f00;

	public static final int TRANSITION_MESS=0x00002000;
	public static final int REG_MEMB_MESS=0x00001000;
	public static final int CAUSED_BY_NETWORK=0x00000800;
	public static final int CAUSED_BY_DISCONNECT=0x00000400;
	public static final int CAUSED_BY_LEAVE=0x00000200;
	public static final int CAUSED_BY_JOIN=0x00000100;

	// FlushSpread
    public static final int DONT_BLOCK=0x10000000;
    public static final int FLUSH_REQ_MESS=0x20000000;
    public static final int SUBGROUP_CAST=0x40000000;
	
	private int serviceFlags;

	public SpService(int service) {
		this.serviceFlags=service;
	}
	
	public SpService(String prop) {
		setService(prop);
	}

	public void setService(String prop) {
		if (prop.equals("unreliable"))
			serviceFlags = SpService.UNRELIABLE_MESS;
		else if (prop.equals("reliable"))
			serviceFlags = SpService.RELIABLE_MESS;
		else if (prop.equals("fifo"))
			serviceFlags = SpService.FIFO_MESS;
		else if (prop.equals("causal"))
			serviceFlags = SpService.CAUSAL_MESS;
		else if (prop.equals("agreed"))
			serviceFlags = SpService.AGREED_MESS;
		else if (prop.equals("safe"))
			serviceFlags = SpService.SAFE_MESS;
	}

	public int getService() {
		return serviceFlags;
	}

	public int compare(Service service) throws UnsupportedServiceException {
		SpService other;
		try {
			other=(SpService)service;
		} catch(ClassCastException e) {
			throw new UnsupportedServiceException("unsupported service "+service);
		}
		return serviceFlags-other.serviceFlags;
	}

}
