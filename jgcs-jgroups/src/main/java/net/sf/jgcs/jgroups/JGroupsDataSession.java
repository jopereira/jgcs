/*
 * JGroups implementation of JGCS - Group Communication Service
 * Copyright (C) 2006 Nuno Carvalho, Universidade de Lisboa
 * Copyright (C) 2013 José Orlando Pereira
 * 
 * http://github.com/jopereira/jgcs
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
 
package net.sf.jgcs.jgroups;

import java.io.IOException;

import net.sf.jgcs.Annotation;
import net.sf.jgcs.ClosedSessionException;
import net.sf.jgcs.GroupException;
import net.sf.jgcs.Message;
import net.sf.jgcs.Service;
import net.sf.jgcs.annotation.PointToPoint;
import net.sf.jgcs.spi.AbstractDataSession;

import org.jgroups.JChannel;

class JGroupsDataSession extends AbstractDataSession<JGroupsProtocol,JGroupsDataSession,JGroupsControlSession,JGroupsGroup> {

	private JChannel channel;
	
	JGroupsDataSession(JChannel channel)	throws GroupException {
		this.channel = channel;
	}

	@Override
	public Message createMessage() throws GroupException {
		try {
			lock.lock();
			onEntry();
			return new JGroupsMessage();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void multicast(Message msg, Service service, Object cookie,
			Annotation... annotation) throws IOException {
	
		JGroupsMessage m = (JGroupsMessage) msg;
		JGroupsService s = (JGroupsService) service;
		
		for(Annotation a: annotation)
			if (a instanceof PointToPoint)
				m.setDest(((JGroupsSocketAddress)((PointToPoint)a).getDestination()).getAddress());

		m.setFlag(s.getFlags());

		try {
			channel.send(m);
		} catch(IllegalStateException ise) {
			throw new ClosedSessionException("channel closed", ise);
		} catch (Exception e) {
			throw new GroupException("channel exception", e);
		}
	}

	void deliver(JGroupsMessage message, JGroupsService service) {
		notifyListeners(message, service);
	}
}
