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

import net.sf.jgcs.Annotation;
import net.sf.jgcs.GroupException;
import net.sf.jgcs.Message;
import net.sf.jgcs.Service;
import net.sf.jgcs.UnsupportedMessageException;
import net.sf.jgcs.UnsupportedServiceException;
import net.sf.jgcs.corosync.jni.ClosedProcessGroup;
import net.sf.jgcs.corosync.jni.ClosedProcessGroup.Address;
import net.sf.jgcs.spi.AbstractDataSession;

class CPGDataSession extends AbstractDataSession<CPGProtocol,CPGDataSession,CPGControlSession,CPGGroup> {
	
	@Override
	public Message createMessage() throws GroupException {
		onEntry();
		return new CPGMessage();
	}

	@Override
	public void multicast(Message msg, Service service, Object cookie, Annotation... annotation) throws IOException {
		onEntry();
		CPGService guarantee;
		try {
			guarantee = (CPGService) service;
		} catch(ClassCastException e) {
			throw new UnsupportedServiceException(service);
		}
		CPGMessage m;
		try {
			m = (CPGMessage) msg;
		} catch(ClassCastException e) {
			throw new UnsupportedMessageException(msg);
		}

		protocol.cpg.multicast(guarantee.getGuarantee(), m.getPayload());
	}

	void deliver(Address addr, byte[] msg) {
		notifyListeners(new CPGMessage(msg, addr), new CPGService(ClosedProcessGroup.CPG_TYPE_SAFE));
	}
}
