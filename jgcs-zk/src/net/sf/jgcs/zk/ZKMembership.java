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

import net.sf.jgcs.NotJoinedException;
import net.sf.jgcs.membership.Membership;
import net.sf.jgcs.membership.MembershipID;

public class ZKMembership implements Membership {

	private int vid, rank = -1;
	private ArrayList<SocketAddress> members;

	ZKMembership(String me, int vid, String[] members) {
		this.vid = vid;
		this.members = new ArrayList<SocketAddress>();
		for(int i = 0; i<members.length; i++) {
			if (members[i].equals(me))
				rank = i;
			this.members.add(new ZKAddress(members[i]));
		}
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
	public int getLocalRank() throws NotJoinedException {
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
		return Collections.emptyList();
	}

	@Override
	public List<SocketAddress> getLeavedMembers() {
		return Collections.emptyList();
	}

	@Override
	public List<SocketAddress> getFailedMembers() {
		return Collections.emptyList();
	}

	@Override
	public String toString() {
		return "[vid=" + vid + ", members=" + members + "]";
	}
}
