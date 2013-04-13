/*
 * IP Multicast implementation of JGCS - Group Communication Service.
 * Copyright (C) 2006,2013 José Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs.ip;

import net.sf.jgcs.GroupException;
import net.sf.jgcs.Protocol;
import net.sf.jgcs.ProtocolFactory;

class IpProtocolFactory implements ProtocolFactory {

	private static final long serialVersionUID = 2L;

	@Override
	public Protocol createProtocol() throws GroupException{
		return new IpProtocol();
	}
}
