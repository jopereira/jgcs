
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
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;

import net.sf.jgcs.Annotation;
import net.sf.jgcs.ClosedSessionException;
import net.sf.jgcs.GroupException;
import net.sf.jgcs.Message;
import net.sf.jgcs.Service;
import net.sf.jgcs.UnsupportedMessageException;
import net.sf.jgcs.UnsupportedServiceException;
import net.sf.jgcs.spi.AbstractPollingDataSession;
import net.sf.neem.MulticastChannel;

public class NeEMDataSession extends AbstractPollingDataSession<NeEMProtocol,NeEMDataSession,NeEMControlSession,NeEMGroup> {
	private MulticastChannel sock;
	
	NeEMDataSession(MulticastChannel sock) {
		this.sock=sock;
		boot();
	}

	public Message createMessage() throws GroupException {
		try {
			lock.lock();
			onEntry();
			return new NeEMMessage();
		} finally {
			lock.unlock();
		}
	}

	public void multicast(Message msg, Service service, Object cookie, Annotation... annotation) throws IOException {
		try {
			if (!(msg instanceof NeEMMessage))
				throw new UnsupportedMessageException(msg);
			if (!(service instanceof NeEMService))
				throw new UnsupportedServiceException(service);

			sock.write(ByteBuffer.wrap(msg.getPayload()));
		} catch(ClosedChannelException cce) {
			throw new ClosedSessionException();
		}
	}
	
	protected void read() throws IOException {
		ByteBuffer buf=ByteBuffer.allocate(1024);
		sock.read(buf);
		buf.flip();
		notifyListeners(new NeEMMessage(buf), new NeEMService());		
	}

	protected void cleanup() {
		super.cleanup();
		sock.close();
	}
}
