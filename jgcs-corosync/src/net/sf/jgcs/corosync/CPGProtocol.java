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
	  
package net.sf.jgcs.corosync;

import net.sf.jgcs.JGCSException;
import net.sf.jgcs.corosync.jni.ClosedProcessGroup;
import net.sf.jgcs.corosync.jni.ClosedProcessGroup.Callbacks;
import net.sf.jgcs.corosync.jni.CorosyncException;
import net.sf.jgcs.spi.AbstractPollingProtocol;

public class CPGProtocol extends AbstractPollingProtocol<CPGProtocol,CPGDataSession,CPGControlSession,CPGGroup> {
	
	ClosedProcessGroup cpg;

	CPGProtocol() throws JGCSException {
		cpg = new ClosedProcessGroup(new Callbacks() {
			public void deliver(String group, int nodeid, int pid, byte[] msg) {
				CPGDataSession data=(CPGDataSession) lookupDataSession(new CPGGroup(group));
				data.deliver(nodeid, pid, msg);
			}
			
			@Override
			public void configurationChange(String group, CPGAddress[] members, CPGAddress[] left, int[] lr, CPGAddress[] joined, int[] jr) {
				CPGControlSession control=(CPGControlSession) lookupControlSession(new CPGGroup(group));
				control.install(members, left, lr, joined, jr);
			}

			@Override
			public void ringChange(int nodeid, long seq, int[] nodes) {
				// TODO Auto-generated method stub
			}
		}, 0);
	}

	@Override
	protected synchronized void createSessions(CPGGroup group) {
		putSessions(group, new CPGControlSession(), new CPGDataSession());
	}
	
	@Override
	protected void read() {
		try {
			cpg.dispatch(ClosedProcessGroup.CS_DISPATCH_ONE);
		} catch (CorosyncException e) {
			e.printStackTrace();
		}
	}
}
