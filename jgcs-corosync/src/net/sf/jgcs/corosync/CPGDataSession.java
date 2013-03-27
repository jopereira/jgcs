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
import java.net.SocketAddress;

import net.sf.jgcs.Annotation;
import net.sf.jgcs.ClosedSessionException;
import net.sf.jgcs.Message;
import net.sf.jgcs.Service;
import net.sf.jgcs.UnsupportedServiceException;
import net.sf.jgcs.spi.AbstractDataSession;

public class CPGDataSession extends AbstractDataSession {
	private CPGProtocol protocol; // FIXME: could be removed, if superclass was generic
	
	CPGDataSession(CPGProtocol protocol, CPGGroup group) {
		super(protocol, group);
		this.protocol = protocol;
	}

	public Message createMessage() throws ClosedSessionException {
		return new CPGMessage();
	}

	public void multicast(Message msg, Service service, Object cookie, Annotation... annotation) throws IOException, UnsupportedServiceException {
		CPGService guarantee;
		try {
			guarantee = (CPGService) service;
		} catch(ClassCastException e) {
			// FIXME: missing nested exception
			throw new UnsupportedServiceException(e.toString());
		}
		protocol.cpg.multicast(guarantee.getGuarantee(), msg.getPayload());
	}

	public void send(Message msg, Service service, Object cookie, SocketAddress destination, Annotation... annotation) throws IOException, UnsupportedServiceException {
		throw new UnsupportedServiceException("sending to processes not supported");
	}

	void deliver(int nodeid, int pid, byte[] msg) {
		notifyMessageListeners(new CPGMessage(msg, new CPGAddress(nodeid, pid)));
	}
}
