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

import java.io.IOException;
import java.net.SocketAddress;

import net.sf.jgcs.Annotation;
import net.sf.jgcs.ClosedSessionException;
import net.sf.jgcs.Message;
import net.sf.jgcs.Service;
import net.sf.jgcs.UnsupportedServiceException;
import net.sf.jgcs.spi.AbstractDataSession;
import net.sf.jgcs.zk.groupz.Endpoint;

public class ZKDataSession extends AbstractDataSession {	

	Endpoint endpoint;

	ZKDataSession(ZKProtocol zkProtocol, ZKGroup group) {
		super(zkProtocol, group);
	}

	@Override
	public Message createMessage() throws ClosedSessionException {
		return new ZKMessage();
	}

	@Override
	public void multicast(Message msg, Service service, Object cookie, Annotation... annotation) throws IOException, UnsupportedServiceException {
		endpoint.send(msg.getPayload());
	}

	@Override
	public void send(Message msg, Service service, Object cookie, SocketAddress destination, Annotation... annotation) throws IOException, UnsupportedServiceException {
		// TODO Auto-generated method stub
	}

	void receive(byte[] data) {
		notifyMessageListeners(new ZKMessage(data));
	}
}
