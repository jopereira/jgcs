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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.jgcs.Membership;
import net.sf.jgcs.MembershipID;
import net.sf.jgcs.InvalidStateException;

public class ZKMembership implements Membership {

	private int vid, rank = -1;
	private List<SocketAddress> members;
	private List<SocketAddress> failed, joined, leaved;


	ZKMembership(String me, int vid, String[] members, Membership previous) {
		this.vid = vid;
		this.members = new ArrayList<SocketAddress>();
		for(int i = 0; i<members.length; i++) {
			if (members[i].equals(me))
				rank = i;
			this.members.add(new ZKAddress(members[i]));
		}
		if (previous == null) {
			joined = new ArrayList<SocketAddress>(1);
			joined.add(new ZKAddress(me));
			failed = new ArrayList<SocketAddress>();
		} else {
			joined = new ArrayList<SocketAddress>(this.members);
			joined.removeAll(previous.getMembershipList());
			failed = new ArrayList<SocketAddress>(previous.getMembershipList());
			failed.removeAll(this.members);
		}
		leaved = new ArrayList<SocketAddress>();		

	}

	@Override
	public List<SocketAddress> getMembershipList() {
		return members;
	}

	@Override
	public MembershipID getMembershipID() {
		return new ZKMembershipID(vid);
	}

	@Override
	public int getLocalRank() throws InvalidStateException {
		return rank;
	}

	@Override
	public int getCoordinatorRank() {
		return 0;
	}

	@Override
	public int getMemberRank(SocketAddress peer) {
		return members.indexOf(peer);
	}

	@Override
	public SocketAddress getMemberAddress(int rank) {
		return members.get(rank);
	}

	@Override
	public List<SocketAddress> getJoinedMembers() {
		return joined;
	}

	@Override
	public List<SocketAddress> getLeavedMembers() {
		return leaved;
	}

	@Override
	public List<SocketAddress> getFailedMembers() {
		return failed;
	}

	@Override
	public String toString() {
		return "[vid=" + vid + ", members=" + members + "]";
	}
}
