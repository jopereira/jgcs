/*
 * Corosync/CPG implementation of JGCS - Group Communication Service.
 * Copyright (C) 2013 Universidade do Minho
 *
 * jop@di.uminho.pt - http://www.di.uminho.pt/~jop
 *
 * Departamento de Informatica, Universidade do Minho
 * Campus de Gualtar, 4710-057 Braga, Portugal
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs.corosync.jni;

import net.sf.jgcs.GroupException;

public class CorosyncException extends GroupException {
	private static final long serialVersionUID = 8111489446009578137L;

	private CorosyncException(String string) {
		super(string);
	}	
}
