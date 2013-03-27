/*
 * JGCS - Group Communication Service.
 * Copyright (C) 2006 Nuno Carvalho, Universidade de Lisboa
 *
 * jgcs@lasige.di.fc.ul.pt
 *
 * Departamento de Informatica, Universidade de Lisboa
 * Bloco C6, Faculdade de Ciências, Campo Grande, 1749-016 Lisboa, Portugal.
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs;

import java.io.Serializable;

/**
 * Uniquely identifies each membership configuration shown to the application.
 * 
 * With primary partition protocols, this identifier provides a total order
 * on views. With partitionable protocols, views are not totally ordered and
 * even the strict partial order cannot easily be captured. The implementation
 * of {@link java.lang.Comparable java.lang.Comparable} should not be assumed.
 */
public interface MembershipID extends Serializable {
}
