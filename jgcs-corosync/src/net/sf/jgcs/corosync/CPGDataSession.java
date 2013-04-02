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

import java.io.IOException;

import net.sf.jgcs.Annotation;
import net.sf.jgcs.JGCSException;
import net.sf.jgcs.Message;
import net.sf.jgcs.Service;
import net.sf.jgcs.UnsupportedServiceException;
import net.sf.jgcs.spi.AbstractDataSession;

public class CPGDataSession extends AbstractDataSession<CPGProtocol,CPGDataSession,CPGControlSession,CPGGroup> {
	
	public Message createMessage() throws JGCSException {
		onEntry();
		return new CPGMessage();
	}

	public void multicast(Message msg, Service service, Object cookie, Annotation... annotation) throws IOException {
		onEntry();
		CPGService guarantee;
		try {
			guarantee = (CPGService) service;
		} catch(ClassCastException e) {
			throw new UnsupportedServiceException(service.toString());
		}
		protocol.cpg.multicast(guarantee.getGuarantee(), msg.getPayload());
	}

	void deliver(int nodeid, int pid, byte[] msg) {
		notifyMessageListeners(new CPGMessage(msg, new CPGAddress(nodeid, pid)));
	}
}
