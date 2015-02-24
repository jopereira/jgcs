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

package net.sf.jgcs.zk.groupz;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;

class Messages implements Watcher {
	private Endpoint ep;
	private String path;
	
	private int lastSent=-1, lastRecv=-1, lastStable=-1;
	private List<byte[]> data=new ArrayList<byte[]>();
	
	public Messages(String path, String me, Endpoint ep) throws KeeperException, InterruptedException {
		this.ep=ep;
		this.path=path+"/messages";

		create();
	}

	private void create() throws KeeperException, InterruptedException {
		try {
			ep.zk.create(path, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch (KeeperException.NodeExistsException e) {
			// already done
		}
	}

	@Override
	public void process(WatchedEvent event) {
		ep.wakeup();
	}

	public synchronized List<byte[]> receiveAndGC(int low) throws NumberFormatException, KeeperException, InterruptedException {
		this.lastStable=low;
		update();
		List<byte[]> result=data;
		lastRecv=lastSent;
		data=new ArrayList<byte[]>();
		return result;
	}
	
	private void update() throws KeeperException, InterruptedException {
		SortedSet<String> childs=new TreeSet<String>();
		childs.addAll(ep.zk.getChildren(path, this));
		for(String child: childs) {
			int id=Integer.parseInt(child);
			if (id>lastSent) {
				byte[] value=ep.zk.getData(path+"/"+child, null, null);
				data.add(value);
				lastSent=id;
			} else if (id<=lastStable){
				try {
					ep.zk.delete(path+"/"+child, -1);
				} catch(KeeperException.NoNodeException e) {
					// someone got there first...
				}
			}
		}
	}
	
	public void send(byte[] data) throws KeeperException, InterruptedException {
		ep.zk.create(path+"/", data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
	}
	
	public synchronized int getLastReceived() throws KeeperException, InterruptedException {
		return lastRecv;
	}
	
	public synchronized int getLastSent() throws KeeperException, InterruptedException {
		update();
		return lastSent;
	}
}
