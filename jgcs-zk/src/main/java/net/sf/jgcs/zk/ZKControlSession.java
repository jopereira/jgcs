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
import net.sf.jgcs.GroupException;
import net.sf.jgcs.InvalidStateException;
import net.sf.jgcs.spi.AbstractBlockSession;
import net.sf.jgcs.zk.groupz.Endpoint;
import net.sf.jgcs.zk.groupz.StateException;

class ZKControlSession extends AbstractBlockSession<ZKProtocol,ZKDataSession,ZKControlSession,ZKGroup> {

	Endpoint endpoint;
	private ZKMembership current;
	private boolean blocked;

	@Override
	public void blockOk() throws InvalidStateException, GroupException {
		blocked = true;
		endpoint.blockOk();
	}

	@Override
	public boolean isBlocked() throws InvalidStateException {
		/* Don't bother with synchronized, since this is inherently 
		 * racy and should be deprecated.
		 */
		return blocked;
	}

	@Override
	public void join() throws GroupException {
		try {
			endpoint.join();
		} catch(StateException se) {
			if (se.isDisconnected())
				throw new ClosedSessionException();
			throw new InvalidStateException();
		}
	}

	@Override
	public void leave() throws GroupException {
		lock.lock();
		if (isClosed())
			throw new ClosedSessionException();
		endpoint.leave();
		lock.unlock();
	}

	@Override
	public SocketAddress getLocalAddress() {
		try {
			return new ZKAddress(endpoint.getProcessId());
		} catch (ZKException e) {
			return null;
		}
	}

	void install(int vid, String[] members) throws ZKException {
		current = new ZKMembership(endpoint.getProcessId(), vid, members, membership);
		blocked = false;
		notifyAndSetMembership(current);
	}

	void block() {
		notifyBlock();
	}
}
