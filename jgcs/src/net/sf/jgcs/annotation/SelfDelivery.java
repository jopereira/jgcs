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

import net.sf.jgcs.Annotation;

/**
 * Inform the protocol whether message delivery to the sender can
 * be omitted. 
 * 
 * This is useful for performance purposes and is directly provided by
 * some protocols. Being an annotation, this is optionally provided
 * by the protocol and the application should still behave correctly
 * even if the annotation is ignored.
 * 
 * @author jop
 */
public enum SelfDelivery implements Annotation {
	/**
	 * Message sent to group may be omitted back to sender.
	 */
	DISCARD,
	
	/**
	 * Delivery of message sent to group may not be omitted on sender.
	 */
	DELIVER;
}
