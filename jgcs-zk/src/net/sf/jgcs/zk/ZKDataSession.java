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

import net.sf.jgcs.Annotation;
import net.sf.jgcs.ClosedSessionException;
import net.sf.jgcs.GroupException;
import net.sf.jgcs.InvalidStateException;
import net.sf.jgcs.Message;
import net.sf.jgcs.Service;
import net.sf.jgcs.UnsupportedMessageException;
import net.sf.jgcs.UnsupportedServiceException;
import net.sf.jgcs.spi.AbstractDataSession;
import net.sf.jgcs.zk.groupz.Endpoint;
import net.sf.jgcs.zk.groupz.StateException;

public class ZKDataSession extends AbstractDataSession<ZKProtocol,ZKDataSession,ZKControlSession,ZKGroup> {	

	Endpoint endpoint;

	@Override
	public Message createMessage() throws GroupException {
		onEntry();
		return new ZKMessage();
	}

	@Override
	public void multicast(Message msg, Service service, Object cookie, Annotation... annotation) throws IOException {
		ZKService s;
		ZKMessage m;
		try {
			s = (ZKService) service;
		} catch(ClassCastException cce) {
			throw new UnsupportedServiceException(service);
		}
		try {
			m = (ZKMessage) msg;
		} catch(ClassCastException cce) {
			throw new UnsupportedMessageException(msg);
		}
		try {
			endpoint.send(msg.getPayload());
		} catch(StateException se) {
			if (se.isDisconnected())
				throw new ClosedSessionException();
			throw new InvalidStateException();
		}
	}

	void receive(byte[] data) {
		notifyListeners(new ZKMessage(data), new ZKService());
	}	
}
