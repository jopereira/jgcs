
/*
 * NeEM implementation of JGCS - Group Communication Service.
 * Copyright (C) 2006 Jose' Orlando Pereira, Universidade do Minho
 *
 * jop@di.uminho.pt - http://gsd.di.uminho.pt/~jop
 * jgcs@lasige.di.fc.ul.pt
 *
 * Departamento de Informatica, Universidade do Minho
 * Campus de Gualtar, 4710-057 Braga, Portugal
 *
 * See COPYING for licensing details.
 */
	  
package net.sf.jgcs.neem;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

import net.sf.neem.MulticastChannel;
import net.sf.jgcs.AbstractPollingDataSession;
import net.sf.jgcs.Annotation;
import net.sf.jgcs.Message;
import net.sf.jgcs.Service;
import net.sf.jgcs.UnsupportedServiceException;

public class NeEMDataSession extends AbstractPollingDataSession {
	private MulticastChannel sock;
	
	NeEMDataSession(MulticastChannel sock, NeEMProtocol protocol, NeEMGroup group) {
		super(protocol, group);
		this.sock=sock;
	}

	public Message createMessage() {
		return new NeEMMessage();
	}

	public void send(Message msg, Service service, Object cookie, SocketAddress destination, Annotation... annotation) throws IOException, UnsupportedServiceException {
		throw new UnsupportedServiceException();
	}
	
	public void multicast(Message msg, Service service, Object cookie, Annotation... annotation) throws IOException, UnsupportedServiceException {
		sock.write(ByteBuffer.wrap(msg.getPayload()));
	}
	
	protected Message read() throws IOException {
		ByteBuffer buf=ByteBuffer.allocate(1024);
		sock.read(buf);
		buf.flip();
		return new NeEMMessage(buf);		
	}

	protected void cleanup() {
		sock.close();
	}
}
