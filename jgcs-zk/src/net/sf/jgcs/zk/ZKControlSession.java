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

import java.net.SocketAddress;

import net.sf.jgcs.ClosedSessionException;
import net.sf.jgcs.JGCSException;
import net.sf.jgcs.NotJoinedException;
import net.sf.jgcs.spi.AbstractBlockSession;
import net.sf.jgcs.zk.groupz.Endpoint;

public class ZKControlSession extends AbstractBlockSession<ZKProtocol,ZKDataSession,ZKControlSession,ZKGroup> {

	Endpoint endpoint;
	private ZKAddress localid;
	private ZKMembership current;
	private boolean blocked;

	@Override
	public void blockOk() throws NotJoinedException, JGCSException {
		blocked = true;
		endpoint.blockOk();
	}

	@Override
	public boolean isBlocked() throws NotJoinedException {
		/* Don't bother with synchronized, since this is inherently 
		 * racy and should be deprecated.
		 */
		return blocked;
	}

	@Override
	public synchronized void join() throws JGCSException {
		onEntry();
		endpoint.join();
		localid = new ZKAddress(endpoint.getProcessId());
	}

	@Override
	public synchronized void leave() throws JGCSException {
		onEntry();
		endpoint.leave();
	}

	@Override
	public boolean isJoined() {
		return current != null;
	}

	@Override
	public SocketAddress getLocalAddress() {
		return localid;
	}

	void install(int vid, String[] members) {
		current = new ZKMembership(localid.getName(), vid, members);
		blocked = false;
		notifyAndSetMembership(current);
	}

	void block() {
		notifyBlock();
	}
}
