/*
   Copyright 2010-2013 José Orlando Pereira <jop@di.uminho.pt>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package net.sf.jgcs.zk;

import java.io.IOException;

import net.sf.jgcs.AbstractProtocol;
import net.sf.jgcs.ControlSession;
import net.sf.jgcs.DataSession;
import net.sf.jgcs.GroupConfiguration;
import net.sf.jgcs.JGCSException;
import net.sf.jgcs.zk.groupz.Application;
import net.sf.jgcs.zk.groupz.Endpoint;

import org.apache.zookeeper.ZooKeeper;

public class ZKProtocol extends AbstractProtocol {
	private ZooKeeper zk;

	ZKProtocol(String connectString, int sessionTimeout) throws JGCSException {
		try {
			zk = new ZooKeeper(connectString, sessionTimeout, null);
		} catch (IOException e) {
			throw new ZKException("cannot connect to ZooKeeper", e);
		}
	}

	private synchronized void createSessions(ZKGroup group) throws ZKException {
		final ZKControlSession cs = new ZKControlSession(this, group);
		final ZKDataSession ds = new ZKDataSession(this, group);
		
		Endpoint ep = new Endpoint(zk, group.getGroupId(), new Application() {
			
			@Override
			public void receive(byte[] data) throws ZKException {
				ds.receive(data);
			}
			
			@Override
			public void install(int vid, String[] members) throws ZKException {
				cs.install(vid, members);
				
			}
			
			@Override
			public void block() throws ZKException {
				cs.block();
			}
		});
		
		cs.endpoint = ep;
		ds.endpoint = ep;
		
		putSessions(group, cs, ds);
	}

	@Override
	public DataSession openDataSession(GroupConfiguration group) throws JGCSException {
		DataSession data=lookupDataSession(group);
		if (data==null) {
			createSessions((ZKGroup)group);
			data=lookupDataSession(group);
		}
		return data;
	}

	@Override
	public ControlSession openControlSession(GroupConfiguration group) throws JGCSException {
		ControlSession control=lookupControlSession(group);
		if (control==null) {
			createSessions((ZKGroup)group);
			control=lookupControlSession(group);
		}
		return control;
	}
}
