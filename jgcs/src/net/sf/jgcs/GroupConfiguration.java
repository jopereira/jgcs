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
 * This class defines a GroupConfiguration.
 * Interface that provides a Group configuration to open 
 * {@link net.sf.jgcs.DataSession Sessions}.
 * This Interface must be used together with the {@link net.sf.jgcs.Protocol Protocol}
 * to create a {@link DataSession DataSession} and a {@link ControlSession ControlSession}. 
 * 
 * @see DataSession
 * @see ControlSession
 * @see Protocol
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public interface GroupConfiguration extends Serializable {

}
