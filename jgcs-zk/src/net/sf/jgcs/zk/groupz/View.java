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

import java.util.Arrays;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;

class View implements Watcher {
	private Endpoint ep;
	private String path;
	
	private List<String> data;
	
	public View(String path, Endpoint ep) throws KeeperException, InterruptedException {
		this.ep=ep;
		this.path=path;
		
		if (ep.zk.exists(path, this)!=null)
			update();
	}

	public void propose(List<String> mine) throws KeeperException, InterruptedException {
		String value=null;
		for(String v: mine)
			if (value==null)
				value=v;
			else
				value+=","+v;
		try {
			ep.zk.create(path, value.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch (KeeperException.NodeExistsException e) {
			// not mine...
		}
		update();
	}

	private void update() throws KeeperException, InterruptedException {
		synchronized (this) {
			try {
				byte[] value=ep.zk.getData(path, this, null);
				String[] procs = new String(value).split(",");
				data = Arrays.asList(procs);
			} catch (KeeperException.NoNodeException e) {
				// not yet
			}
		}
	}
	
	public synchronized List<String> getProcesses() throws KeeperException, InterruptedException {
		if (data==null)
			update();
		return data;
	}
	
	public synchronized boolean isDecided() throws KeeperException, InterruptedException {
		if (data==null)
			update();
		return data!=null;
	}

	@Override
	public void process(WatchedEvent event) {
		ep.wakeup();
	}

	@Override
	public String toString() {
		return "["+path+": "+data+"]";
	}
}
