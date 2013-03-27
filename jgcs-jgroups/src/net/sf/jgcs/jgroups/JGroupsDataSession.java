
/*
 *
 * JGroups implementation of JGCS - Group Communication Service
 * Copyright (C) 2006 Nuno Carvalho, Universidade de Lisboa
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
 * 
 * Contact
 * 	Address:
 * 		LASIGE, Departamento de Informatica, Bloco C6
 * 		Faculdade de Ciencias, Universidade de Lisboa
 * 		Campo Grande, 1749-016 Lisboa
 * 		Portugal
 * 	Email:
 * 		jgcs@lasige.di.fc.ul.pt
 * 
 */
 
package net.sf.jgcs.jgroups;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.HashMap;

import net.sf.jgcs.Annotation;
import net.sf.jgcs.ClosedSessionException;
import net.sf.jgcs.GroupConfiguration;
import net.sf.jgcs.JGCSException;
import net.sf.jgcs.Message;
import net.sf.jgcs.Service;
import net.sf.jgcs.UnsupportedServiceException;
import net.sf.jgcs.spi.AbstractDataSession;
import net.sf.jgcs.spi.AbstractProtocol;

import org.apache.log4j.Logger;
import org.jgroups.JChannel;

public class JGroupsDataSession extends AbstractDataSession {

	private static Logger logger = Logger.getLogger(JGroupsDataSession.class);
	
	private HashMap<JGroupsService,JChannel> channelsMap;
	
	public JGroupsDataSession(AbstractProtocol proto, JChannel channel, GroupConfiguration group) 
	throws JGCSException {
		super(proto,group);
		channelsMap = new HashMap<JGroupsService,JChannel>();
		channelsMap.put(new JGroupsService("vsc+total"),channel);
	}

	public void close() {
		//channel.disconnect();
	}

	public Message createMessage() throws ClosedSessionException {
		return new JGroupsMessage();
	}

	public void multicast(Message msg, Service service, Object cookie,
			Annotation... annotation) throws IOException,
			UnsupportedServiceException {
		// TODO: cookie and annotations are NOT used...
		logger.debug("Service on send: "+((JGroupsService)service).getService());
		if( ! (service instanceof JGroupsService))
			throw new UnsupportedServiceException("Service "+service+" is not supported.");
		//FIXME: THIS IS NOT CORRECT!
		// I have to get some how the service that is provided by the channel and compare
		// this service with the incoming service.
		JChannel channel = channelsMap.get(service);
		if(channel == null)
			throw new UnsupportedServiceException("There is no JGroups channel for the service "+service);
		try {
			channel.send((org.jgroups.Message) msg);
		} catch (Exception e) {
			throw new JGCSException("Cannot send message.",e);
		}
	}

	public void send(Message msg, Service service, Object cookie,
			SocketAddress destination, Annotation... annotation)
			throws IOException, UnsupportedServiceException {
		// TODO: cookie and annotations are NOT used...
		logger.debug("Service on send: "+((JGroupsService)service).getService());
		if( ! (service instanceof JGroupsService))
			throw new UnsupportedServiceException("Service "+service+" is not supported.");
		//FIXME: THIS IS NOT CORRECT!
		// I have to get some how the service that is provided by the channel and compare
		// this service with the incoming service.
		JChannel channel = channelsMap.get(service);
		if(channel == null)
			throw new UnsupportedServiceException("There is no JGroups channel for the service "+service);
		((org.jgroups.Message) msg).setDest(((JGroupsSocketAddress)destination).getAddress());		
		try {
			channel.send((org.jgroups.Message) msg);
		} catch (Exception e) {
			throw new JGCSException("Cannot send message.",e);
		}
	}
	
	public void notifyExceptionListeners(JGCSException exception) {
		super.notifyExceptionListeners(exception);
	}

	public Object notifyMessageListeners(Message msg) {
		return super.notifyMessageListeners(msg);
	}
	
	public void notifyServiceListeners(Object context, Service service) {
		super.notifyServiceListeners(context,service);
	}

}
