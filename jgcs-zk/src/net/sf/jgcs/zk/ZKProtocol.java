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

import net.sf.jgcs.GroupException;
import net.sf.jgcs.spi.AbstractProtocol;
import net.sf.jgcs.zk.groupz.Application;
import net.sf.jgcs.zk.groupz.Endpoint;

import org.apache.zookeeper.ZooKeeper;

public class ZKProtocol extends AbstractProtocol<ZKProtocol,ZKDataSession,ZKControlSession,ZKGroup> {
	private ZooKeeper zk;

	ZKProtocol(String connectString, int sessionTimeout) throws GroupException {
		try {
			zk = new ZooKeeper(connectString, sessionTimeout, null);
		} catch (IOException e) {
			throw new ZKException("cannot connect to ZooKeeper", e);
		}
	}

	@Override
	protected void createSessions(ZKGroup group) throws ZKException {
		final ZKControlSession cs = new ZKControlSession();
		final ZKDataSession ds = new ZKDataSession();
		
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
	protected void cleanup() {
		super.cleanup();
		try {
			zk.close();
		} catch (InterruptedException e) {
			/* don't care, we're cleaning up */
		}
	}
}
