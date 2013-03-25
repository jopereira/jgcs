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

import net.sf.jgcs.JGCSException;
import net.sf.jgcs.Protocol;
import net.sf.jgcs.ProtocolFactory;

public class ZKProtocolFactory implements ProtocolFactory {
	
	private static final long serialVersionUID = 2L;

	private String connectString = "localhost";
	private int sessionTimeout = 3000;

	public ZKProtocolFactory() {
	}
	
	public ZKProtocolFactory(String connectString, int sessionTimeout) {
		this.connectString = connectString;
		this.sessionTimeout = sessionTimeout;
	}

	public String getConnectString() {
		return connectString;
	}

	public void setConnectString(String connectString) {
		this.connectString = connectString;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	@Override
	public Protocol createProtocol() throws JGCSException {
		return new ZKProtocol(connectString, sessionTimeout);
	}
}
