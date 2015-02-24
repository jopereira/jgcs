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

import java.io.InputStream;
import java.io.OutputStream;

import net.sf.jgcs.GroupException;
import net.sf.jgcs.spi.AbstractProtocol;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.Receiver;
import org.jgroups.View;

class JGroupsProtocol extends AbstractProtocol<JGroupsProtocol,JGroupsDataSession,JGroupsControlSession,JGroupsGroup> {

	private String config;

	JGroupsProtocol(String config) {
		this.config = config;
	}

	@Override
	protected void createSessions(JGroupsGroup g) throws GroupException {
		JGroupsGroup group = (JGroupsGroup) g;

		try {
			JChannel channel = new JChannel(config);
			final JGroupsControlSession cs = new JGroupsControlSession(channel);
			final JGroupsDataSession ds= new JGroupsDataSession(channel);
			putSessions(group, cs,ds);
			
			Receiver recv = new Receiver() {

				@Override
				public void receive(Message msg) {
					ds.deliver(
							new JGroupsMessage(msg.getBuffer(), new JGroupsSocketAddress(msg.getSrc())),
							new JGroupsService(msg.getFlags())
						);
				}

				@Override
				public void getState(OutputStream output) throws Exception {
					// TODO Auto-generated method stub
				}

				@Override
				public void setState(InputStream input) throws Exception {
					// TODO Auto-generated method stub					
				}

				@Override
				public void viewAccepted(View new_view) {
					cs.jgroupsViewAccepted(new_view);						
				}

				@Override
				public void suspect(Address suspected_mbr) {
					// TODO Auto-generated method stub
				}

				@Override
				public void block() {
					// TODO Auto-generated method stub
				}

				@Override
				public void unblock() {
					// TODO Auto-generated method stub					
				}				
			};
			
			channel.setReceiver(recv);

		} catch (Exception e) {
			throw new GroupException("Could not create JGroups channel. ",e);
		}
	}
}
