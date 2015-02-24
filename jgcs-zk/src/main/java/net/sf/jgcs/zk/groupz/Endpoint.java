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

import net.sf.jgcs.zk.ZKException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Group communication end-point. It provides virtually synchronous closed
 * group communication, including view synchrony and totally ordered multicast.
 *  
 * @author jop
 */
public class Endpoint {
	static Logger logger = LoggerFactory.getLogger(Endpoint.class);
	
	ZooKeeper zk;
	private static final String root="/vsc";
	private String path;
	private String me;

	private int vid;
	private Acknowledgments active;
	private Acknowledgments blocked, oldblocked;
	private View current, next;
	
	private Messages messages;
	private boolean awake;
	private Application app;
	
	public enum State { CONNECTED, JOINED, BLOCKING, BLOCKED, DISCONNECTED };
	private State state;
	private Exception cause;

	/**
	 * Initialize a group communication end-point.
	 * 
	 * @param gid a group identifier
	 * @param cb application callbacks
	 * @throws ZKException if a local ZooKeeper server cannot be used
	 */
	public Endpoint(ZooKeeper zk, String gid, Application cb) {
		this.zk=zk;
		this.path=root+"/group/"+gid;
		this.app=cb;
		this.state=State.CONNECTED;
		logger.info("created endpoint on group "+gid);
	}

	/* -- Main VSC state-machine */
	
	// Pre-condition for start changing a view
	private boolean readyToBlock() throws KeeperException, InterruptedException {
		return state==State.JOINED && oldblocked.processSet().isEmpty() &&
			(active.processSet().size()<current.getProcesses().size() || !blocked.processSet().isEmpty());
	}
		
	// Output action to start changing a view
	private void block() throws KeeperException, InterruptedException, ZKException {
		synchronized (this) {
			if (!readyToBlock()) return;
			
			state = State.BLOCKING;

			logger.info("leaving view "+vid);
		}
		
		// Callback out of synchronized!
		app.block();
	}
	
	/**
	 * Allow view change to proceed, after the block() callback has
	 * been invoked. This means that the application cannot send more
	 * messages until a new view has been installed. 
	 * 
	 * @throws ZKException if the group is not trying to block
	 */
	public synchronized void blockOk() throws ZKException {
		onEntry(State.BLOCKING);

		try {
			state = State.BLOCKED;
			blocked.create(messages.getLastReceived());
			active.remove();
				
			next = new View(path+"/"+(vid+1), this);

			logger.info("blocked on view "+vid);
		} catch(KeeperException e) {
			onExit(e);
		} catch (InterruptedException e) {
			onExit(e);
		}
	}

	// Pre-condition for installing a new view
	private boolean readyToInstall() throws KeeperException, InterruptedException {
		return state==State.BLOCKED && active.processSet().isEmpty() &&
			// A new process can only propose to change a dead view
			((messages==null && getLastStableMessage()==Integer.MAX_VALUE) ||
					
			// If I was in the view, I know how many messages have been sent
			 (messages!=null && getLastStableMessage()>=messages.getLastSent()));
	}
	
	// Output action for installing a view
	private void install() throws KeeperException, InterruptedException, ZKException {
		String[] names=null; 

		synchronized (this) {
			if (!readyToInstall()) return;
			
			// Garbage collect and verify view-synchrony
			if (messages!=null && !messages.receiveAndGC(getLastStableMessage()).isEmpty())
				throw new ZKException("there is a bug somewhere", null);

			List<String> prop=new ArrayList<String>();
			// Respect order in previous view
			for(String s: current.getProcesses())
				if (blocked.processSet().contains(s))
					prop.add(s);
			// Arriving processes in any order
			for(String s: blocked.processSet())
				if (!current.getProcesses().contains(s))
					prop.add(s);
			
			next.propose(prop);		
			
			vid ++;

			oldblocked=blocked;

			active = new Acknowledgments(path+"/"+vid+"/active", me, this);
			blocked = new Acknowledgments(path+"/"+vid+"/blocked", me, this);
			
			if (next.getProcesses().contains(me)) {
				current = next;
				next = null;
				messages = new Messages(path+"/"+vid, me, this);
				active.create(-1);
				state = State.JOINED;
				names = getCurrentView();
			} else {
				messages = null;
				cleanup(null);
			}

			oldblocked.remove();
		
			logger.info("installing view "+vid);
		}
		
		// Call install out of synchronized
		app.install(vid, names);
	}
		
	/* -- Joining and leaving a group */
	
	private void createPath(String path) throws KeeperException, InterruptedException {
		try {
			zk.create(path, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch(KeeperException.NodeExistsException e) {
			// already exists, don't care
		}
	}
	
	private void findPid() throws KeeperException, InterruptedException {
		String[] path = zk.create(root+"/process/", new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL).split("/");
		me = path[path.length-1];
		logger.info("my process id is "+me);
	}
			
	private void boot() throws KeeperException, InterruptedException, ZKException {
		vid=0;

		createPath(root);
		createPath(root+"/group");
		createPath(root+"/process");
		createPath(path);
		createPath(path+"/0");

		logger.info("new group created");
	}
	
	private int findView() {
		vid=-1;
		try {
			for(String svid: zk.getChildren(path, false)) {
				int pvid=Integer.parseInt(svid);
				if (pvid>vid)
					vid=pvid;
			}
		} catch (Exception e) {
			// not ready
		}
		return vid;
	}

	/**
	 * Join the group. This blocks the calling thread until an initial view is
	 * installed.
	 * 
	 * @throws ZKException if the end-point is not freshly created.
	 */
	public synchronized void join() throws ZKException {
		onEntry(State.CONNECTED);
				
		try {
			int targetvid=findView();

			if (vid<0)
				boot();
			
			findPid();

			current = new View(path+"/"+vid, this);
			blocked = new Acknowledgments(path+"/"+vid+"/blocked", me, this);
			active = new Acknowledgments(path+"/"+vid+"/active", me, this);
			next = new View(path+"/"+(vid+1), this);

			blocked.create(Integer.MAX_VALUE);
			
			state=State.BLOCKED;
				
			logger.info("joining group");

			new Thread(new Runnable() {
				public void run() {
					loop();
				}
			}).start();
			while(vid<=targetvid && state!=State.DISCONNECTED)
				wait();
		} catch(Exception e) {
			onExit(e);
		}

		if (state==State.DISCONNECTED)
			throw new ZKException("failed to join", cause);
	}

	/**
	 * Leave the group. The end-point cannot be used again.
	 */
	public synchronized void leave() {
		cleanup(null);
	}

	/* -- Message handling */
	
	private int getLastStableMessage() throws KeeperException, InterruptedException {
		int lowa=active.get();
		int lowb=blocked.get();
		return lowa<lowb?lowa:lowb;
	}

	// Pre-condition for delivering messages
	private boolean readyToDeliver() {
		return (state==State.JOINED || state==State.BLOCKING || state==State.BLOCKED) &&
			messages!=null;
	}
	
	// Action for delivering messages
	private void deliver() throws KeeperException, InterruptedException, ZKException {
		List<byte[]> values;
		
		synchronized (this) {
			if (!readyToDeliver()) return;
			
			values=messages.receiveAndGC(getLastStableMessage());
		}

		if (values.size()>0)
			logger.debug("delivering "+values.size()+" messages");

		for(byte[] value: values)
			app.receive(value);
		
		synchronized (this) {
			if (next==null)
				active.set(messages.getLastReceived());
			else
				blocked.set(messages.getLastReceived());
		}
	}
	
	/**
	 * Send a message. This cannot be invoked after blockOk() has been called
	 * until a new view is installed.
	 * 
	 * @throws ZKException if the end-point is not freshly created.
	 */
	public synchronized void send(byte[] data) throws ZKException {
		onEntry(State.JOINED, State.BLOCKING);
		
		try {
			messages.send(data);
		} catch (KeeperException e) {
			onExit(e);
		} catch (InterruptedException e) {
			onExit(e);
		}
	}
	
	/* -- The rest of the public API -- */
	
	/**
	 * Get a unique identifier of the local process.
	 * 
	 * @return the identification of the local process
	 * @throws ZKException if no view is installed
	 */
	public synchronized String getProcessId() throws ZKException {
		onEntry(State.JOINED, State.BLOCKING, State.BLOCKED);		
		return me;
	}
	
	/**
	 * Get the current composition of the view. This is guaranteed to 
	 * be exactly the same in all members.
	 * 
	 * @return the list of group members
	 * @throws ZKException if no view is installed
	 */
	public synchronized String[] getCurrentView() throws ZKException {
		onEntry(State.JOINED, State.BLOCKING, State.BLOCKED);
		try {
			return current.getProcesses().toArray(new String[current.getProcesses().size()]);
		} catch(Exception e) {
			onExit(e);
			return null; // never happens, onExit always throws
		}
	}

	/* -- Error handling -- */
	
	private void onEntry(State... reqs) throws ZKException  {
		for(State req: reqs)
			if (req==state)
				return;
		String rl=null;
		for(State req: reqs)
			if (rl==null)
				rl=req.toString();
			else
				rl+=" or "+req;
		
		ZKException e=new StateException(state, rl);
		cleanup(e);
		throw e;
	}

	private void onExit(Exception e) throws ZKException  {
		cleanup(e);
		throw new ZKException("disconnected on internal error", e);
	}

	private synchronized void cleanup(Exception cause) {
		if (state==State.DISCONNECTED)
			return;
		this.cause=cause;
		state=State.DISCONNECTED;
		if (cause!=null)
			logger.error("detached from group on error", cause);
		else
			logger.info("detached from group on leave");
		wakeup();
	}
		
	/* -- State-machine main loop -- */
	
	private void loop() {
		try {
			while(true) {
				synchronized (this) {
					while(!awake && state!=State.DISCONNECTED)
						wait();
					if (state==State.DISCONNECTED)
						break;
					awake=false;
				}
				
				// This order should not matter for correctness
				block();
				install();
				deliver();
			}
		} catch(Exception e) {
			cleanup(e);
		}
	}

	synchronized void wakeup() {
		awake=true;
		notifyAll();
	}
}