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
 * A process group identifier. It is used mostly to open
 * {@link net.sf.jgcs.DataSession data} and {@link net.sf.jgcs.ControlSession control}
 * sessions associated with that group on some {@link net.sf.jgcs.Protocol protocol}.
 * Its content is entirely specified by each protocol.
 */
public interface GroupConfiguration extends Serializable {
}
