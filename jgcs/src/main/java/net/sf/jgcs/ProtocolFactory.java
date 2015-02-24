/*
 * JGCS - Group Communication Service.
 * Copyright (C) 2006 Nuno Carvalho, Universidade de Lisboa
 * Copyright (C) 2013 Jose' Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs;

import java.io.Serializable;

/**
 * A protocol factory that can create instances of protocols. It holds 
 * the configuration to setup or connect to a protocol stack.
 */
public interface ProtocolFactory extends Serializable {

	/**
	 * Creates a new protocol instance or connection.
	 * @return a new protocol
	 * @throws GroupException protocol specific exception
	 */
	public Protocol createProtocol() throws GroupException;
	
}
