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

import net.sf.jgcs.Message;

class ZKMessage implements Message {

	private static final long serialVersionUID = 2L;
	
	private byte[] data;

	ZKMessage() {
	}

	ZKMessage(byte[] data) {
		this.data = data;
	}

	@Override
	public void setPayload(byte[] buffer) {
		data = buffer;
	}

	@Override
	public byte[] getPayload() {
		return data;
	}

	@Override
	public SocketAddress getSenderAddress() {
		return null;
	}
}
