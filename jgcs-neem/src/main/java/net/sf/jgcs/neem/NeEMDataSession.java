/*
 * NeEM implementation of JGCS - Group Communication Service.
 * Copyright (C) 2006,2013 José Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
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
import net.sf.jgcs.spi.AbstractPollingDataSession;
import net.sf.neem.MulticastChannel;

class NeEMDataSession extends AbstractPollingDataSession<NeEMProtocol,NeEMDataSession,NeEMControlSession,NeEMGroup> {
	private MulticastChannel sock;
	
	NeEMDataSession(MulticastChannel sock) {
		this.sock=sock;
		boot();
	}

	@Override
	public Message createMessage() throws GroupException {
		try {
			lock.lock();
			onEntry();
			return new NeEMMessage();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void multicast(Message msg, Service service, Object cookie, Annotation... annotation) throws IOException {
		try {
			NeEMMessage m = (NeEMMessage) msg;
			NeEMService s = (NeEMService) service;
			sock.write(ByteBuffer.wrap(m.getPayload()));
		} catch(ClosedChannelException cce) {
			throw new ClosedSessionException();
		}
	}
	
	@Override
	protected void read() throws IOException {
		ByteBuffer buf=ByteBuffer.allocate(1024);
		sock.read(buf);
		buf.flip();
		notifyListeners(new NeEMMessage(buf), new NeEMService());		
	}

	@Override
	protected void cleanup() {
		super.cleanup();
		sock.close();
	}
}
