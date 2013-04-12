/*
 * Corosync/CPG implementation of JGCS - Group Communication Service.
 * Copyright (C) 2013 José Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 *
 * See COPYING for licensing details.
 */
	  
package net.sf.jgcs.corosync;

import java.io.IOException;

import net.sf.jgcs.GroupException;
import net.sf.jgcs.corosync.jni.ClosedProcessGroup;
import net.sf.jgcs.corosync.jni.ClosedProcessGroup.Callbacks;
import net.sf.jgcs.spi.AbstractPollingProtocol;

public class CPGProtocol extends AbstractPollingProtocol<CPGProtocol,CPGDataSession,CPGControlSession,CPGGroup> {
	
	ClosedProcessGroup cpg;
	
	CPGProtocol() throws GroupException {
		
		cpg = new ClosedProcessGroup(new Callbacks() {
			public void deliver(String group, int nodeid, int pid, byte[] msg) {
				try {
					CPGDataSession data=(CPGDataSession) lookupDataSession(new CPGGroup(group));
					if (data!=null)
						data.deliver(nodeid, pid, msg);
				} catch(GroupException e) {
					// Session not found. Discard message.
				}
			}
			
			@Override
			public void configurationChange(String group, CPGAddress[] members, CPGAddress[] left, int[] lr, CPGAddress[] joined, int[] jr) {
				try {
					CPGControlSession control=(CPGControlSession) lookupControlSession(new CPGGroup(group));
					if (control!=null)
						control.install(members, left, lr, joined, jr);
				} catch(GroupException e) {
					// Session not found. Discard change.
				}
			}

			@Override
			public void ringChange(int nodeid, long seq, int[] nodes) {
				// TODO Auto-generated method stub
			}
		}, 0);
		boot();
	}

	@Override
	protected void createSessions(CPGGroup group) {
		putSessions(group, new CPGControlSession(), new CPGDataSession());
	}
	
	@Override
	protected void read() throws GroupException {
		cpg.dispatch(ClosedProcessGroup.CS_DISPATCH_ONE);
	}
	
	@Override
	protected void cleanup() {
		super.cleanup();
		try {
			cpg.close();
		} catch (IOException e) {
			/* discard, as we're cleaning up */
		}
		cpg = null;
	}
}
