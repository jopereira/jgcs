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
 * 
 * This class defines a Service.
 * 
 * A Service is some functionality that the channel needs to provide to the application.
 * One example is the optimistic total order. If an application creates a channel that provides
 * optimistic total order, the application will receive the message payload with out guarantees and
 * will be notified later about optimistic delivery, regular delivery, uniform delivery, etc.
 * These notifications must implement this interface.
 * 
 * All related services must be comparable with each other (e.g. uniform delivery is a stronger service than
 * regular delivery, so if the message is uniform, it's also regular and optimistic 
 * -- optimistic lower than regular lower than uniform).
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public interface Service extends Serializable {
	
	/**
	 * Compares two Services of the same protocol. This imposes a partial
	 * order on Services.
	 * 
	 * @param target the service to compare.
	 * @return true if this service is stronger or equal to the target 
	 */
	public boolean satisfies(Service target);
}
