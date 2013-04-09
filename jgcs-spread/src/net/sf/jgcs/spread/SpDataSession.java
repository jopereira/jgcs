
/*
 * Spread implementation of JGCS - Group Communication Service.
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
	  
package net.sf.jgcs.spread;

import java.io.IOException;
import java.nio.ByteBuffer;

import net.sf.jgcs.Annotation;
import net.sf.jgcs.ClosedSessionException;
import net.sf.jgcs.GroupException;
import net.sf.jgcs.Message;
import net.sf.jgcs.Service;
import net.sf.jgcs.UnsupportedMessageException;
import net.sf.jgcs.UnsupportedServiceException;
import net.sf.jgcs.annotation.PointToPoint;
import net.sf.jgcs.annotation.SelfDelivery;
import net.sf.jgcs.spi.AbstractDataSession;
import net.sf.jgcs.spread.jni.Mailbox;

public class SpDataSession extends AbstractDataSession<SpProtocol,SpDataSession,SpControlSession,SpGroup> {
	private Mailbox mb;
	
	SpDataSession(Mailbox mb) {
		this.mb=mb;
	}

	public Message createMessage() throws GroupException {
		try {
			lock.lock();
			onEntry();
			return new SpMessage();
		} finally {
			lock.unlock();
		}
	}

	public void multicast(Message msg, Service service, Object cookie, Annotation... annotation) throws IOException {
		int ret = 0;
		int qos = 0;
		try {
			qos = ((SpService)service).getService();
		} catch(ClassCastException cce) {
			throw new UnsupportedServiceException(service);
		}
		Message m;
		try {
			m = (SpMessage) msg;
		} catch(ClassCastException cce) {
			throw new UnsupportedMessageException(msg);
		}
		String dest = null;
		for(Annotation a: annotation)
			if (a instanceof PointToPoint)
				dest = ((SpGroup)((PointToPoint)a).getDestination()).getGroup();
			else if (a.equals(SelfDelivery.DISCARD))
				qos = qos | SpService.SELF_DISCARD;
		ByteBuffer mess=ByteBuffer.wrap(m.getPayload());

		if (dest == null) {
			dest=((SpGroup)getGroup()).getGroup();
			Mailbox.MulticastArgs info=new Mailbox.MulticastArgs(qos, ((SpGroup)getGroup()).getGroup(), new String[]{dest}, (short) 0);
			ret=mb.C_multicast(info, mess);
		} else {
			Mailbox.MulticastArgs info=new Mailbox.MulticastArgs(qos, ((SpGroup)getGroup()).getGroup(), new String[]{dest}, (short) 0);
			ret=mb.C_subgroupcast(info, mess);
		}
		if (ret<0) {
			if (ret == SpException.ILLEGAL_SESSION)
				throw new ClosedSessionException();
			throw new SpException(ret, null);
		}
	}

	void deliverMessage(Mailbox.ReceiveArgs info, ByteBuffer mess) {
		notifyListeners(new SpMessage(mess, new SpGroup(info.sender)), new SpService(info.service_type));
	}
}
