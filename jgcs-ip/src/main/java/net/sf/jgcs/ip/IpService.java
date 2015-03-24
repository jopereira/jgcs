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

/**
 * Service configuration for IP multicast. The only configuration available is the
 * time-to-live (TTL).
 */
public class IpService implements Service {
	
	private static final long serialVersionUID = 2L;
	
	private int ttl = 1;
	
	/**
	 * Create default service (TTL = 1).
	 */
	public IpService() {
	}

	/**
	 * Create service.
	 * @param ttl time to live for multicast messages.
	 */
	public IpService(int ttl) {
		this.ttl=ttl;
	}

	/**
	 * Create service.
	 * @param ttl time to live for multicast messages.
	 */
	public IpService(String ttl) {
		this.ttl=Integer.parseInt(ttl);
	}

	/**
	 * Get time to live.
	 * @return message time to live.
	 */
	public int getTtl() {
		return ttl;
	}

	/**
	 * Set time to live.
	 * @param ttl message time to live.
	 */
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}
	
	@Override
	public boolean satisfies(Service service) {
		return service instanceof IpService;
	}
}
