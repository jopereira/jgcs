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
 * Uniquely identifies each membership configuration.
 * 
 * With primary partition protocols, this identifier provides a total order
 * on views. With partitionable protocols, views are not totally ordered and
 * even the strict partial order cannot easily be captured. The implementation
 * of {@link java.lang.Comparable java.lang.Comparable} should not be assumed.
 */
public interface MembershipID extends Serializable {
}
