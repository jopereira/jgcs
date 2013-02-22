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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;

class Acknowledgments implements Watcher {
	protected Endpoint ep;
	private String path;
	
	private Map<String,Integer> data;
	private String me;

	public Acknowledgments(String path, String me, Endpoint ep) throws KeeperException, InterruptedException {
		this.ep=ep;
		this.path=path;
		this.me=me;

		try {
			ep.zk.create(path, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch(KeeperException.NodeExistsException e) {
			// don't care
		}
		update();
	}

	@Override
	public void process(WatchedEvent event) {
		ep.wakeup();
	}
	
	private synchronized void update() throws KeeperException, InterruptedException {
		Map<String,Integer> newdata=new HashMap<String, Integer>();
		for(String child: ep.zk.getChildren(path, this)) {
			try {
				byte[] value=ep.zk.getData(path+"/"+child, this, null);
				newdata.put(child, Integer.parseInt(new String(value)));
			} catch (KeeperException.NoNodeException e) {
				// Ignore this one...
			}
		}
		if (data==null || !data.equals(newdata))
			data=newdata;
	}
	
	public synchronized void create(int value) throws KeeperException, InterruptedException {
		ep.zk.create(path+"/"+me, Integer.toString(value).getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
	}
	
	public synchronized void set(int value) throws KeeperException, InterruptedException {
		update();
		if (value<=data.get(me))
			return;
		ep.zk.setData(path+"/"+me, Integer.toString(value).getBytes(), -1);		
	}

	public synchronized void remove() throws InterruptedException, KeeperException {
		try {
			ep.zk.delete(path+"/"+me, -1);
		} catch(KeeperException.NoNodeException e) {
			// don't care
		}
	}
	
	public synchronized int get() throws KeeperException, InterruptedException {
		update();
		int min=Integer.MAX_VALUE;
		for(int i: data.values()) {
			if (i<min)
				min=i;
		}
		return min;
	}
	
	public synchronized Set<String> processSet() throws KeeperException, InterruptedException {
		update();
		return data.keySet();
	}
	
	public String toString() {
		return "["+path+": "+data+"]";
	}
}
