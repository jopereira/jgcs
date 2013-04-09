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

import net.sf.jgcs.zk.ZKException;
import net.sf.jgcs.zk.groupz.Endpoint.State;

/**
 * Operation cannot be performed in this state.
 * 
 * @author jop
 */
public class StateException extends ZKException {

	private static final long serialVersionUID = -8346304783464624788L;
	
	private State found;

	public StateException(Endpoint.State found, String expected) {
		super("the group is "+found+", should be "+expected);
		this.found = found;
	}
		
	public boolean isDisconnected() {
		return found.equals(Endpoint.State.DISCONNECTED);
	}
}
