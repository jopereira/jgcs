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

import net.sf.jgcs.membership.MembershipID;

public class ZKMembershipID implements MembershipID {

	private int vid;

	ZKMembershipID(int vid) {
		this.vid = vid;
	}

	@Override
	public int compareTo(MembershipID arg0) {
		return vid - ((ZKMembershipID) arg0).vid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + vid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ZKMembershipID other = (ZKMembershipID) obj;
		if (vid != other.vid)
			return false;
		return true;
	}
}
