/*
 * JGCS - Group Communication Service.
 * Copyright (C) 2006 Nuno Carvalho, Universidade de Lisboa
 * Copyright (C) 2013 Jose' Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs.annotation;

import java.net.SocketAddress;

import net.sf.jgcs.Annotation;

/**
 * Inform the protocol that the message can be delivered to a single
 * destination.
 * 
 * This is useful for performance purposes and is directly provided by
 * some protocols. Being an annotation, this is optionally provided
 * by the protocol and the application should still behave correctly
 * even if the annotation is ignored.
 */
public class PointToPoint implements Annotation {
	private SocketAddress target;
	
	/**
	 * @param target address of target process
	 */
	public PointToPoint(SocketAddress target) {
		this.target = target;
	}
	
	/**
	 * Get message destination.
	 * @return address of target process
	 */
	public SocketAddress getDestination() {
		return target;
	}
}