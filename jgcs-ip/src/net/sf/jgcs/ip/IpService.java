/*
 * IP Multicast implementation of JGCS - Group Communication Service.
 * Copyright (C) 2006,2013 José Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs.ip;

import net.sf.jgcs.Service;

public class IpService implements Service {
	
	private static final long serialVersionUID = 2L;
	
	private int ttl;
	
	public IpService(String ttl) {
		this.ttl=Integer.parseInt(ttl);
	}
	public int getTtl() {
		return ttl;
	}
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}
	
	@Override
	public boolean satisfies(Service service) {
		return service instanceof IpService;
	}
}
