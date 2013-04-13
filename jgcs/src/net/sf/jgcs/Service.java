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
 * A service defines a delivery guarantee, such as reliability or order.
 * It is used mainly when a message is
 * multicast to inform a protocol session of the desired delivery guarantee.
 * It is also used upon delivery in optmistic protcols, that
 * deliver the payload early and then notify the application when each delivery
 * guarantee has been achieved.
 */
public interface Service extends Serializable {
	/**
	 * Compares two services of the same protocol. This imposes a partial
	 * order on services.
	 * 
	 * @param target the service to compare
	 * @return true if this service is stronger that or equal to the target 
	 */
	public boolean satisfies(Service target);
}
