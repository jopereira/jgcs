
/*
 * Spread implementation of JGCS - Group Communication Service.
 * Copyright (C) 2004,2006 Jose' Orlando Pereira, Universidade do Minho
 *
 * jop@di.uminho.pt - http://gsd.di.uminho.pt/~jop
 * jgcs@lasige.di.fc.ul.pt
 *
 * Departamento de Informatica, Universidade do Minho
 * Campus de Gualtar, 4710-057 Braga, Portugal
 *
 * See COPYING for licensing details.
 */
	  
package net.sf.jgcs.spread;

import java.nio.ByteBuffer;

import net.sf.jgcs.GroupConfiguration;
import net.sf.jgcs.GroupException;
import net.sf.jgcs.spi.AbstractPollingProtocol;
import net.sf.jgcs.spread.jni.Mailbox;

/**
 * Spread protocol. Unicast messages can be received by opening a data session
 * for the local address. 
 * @author jop
 */
public class SpProtocol extends AbstractPollingProtocol<SpProtocol,SpDataSession,SpControlSession,SpGroup> {
	private Mailbox mb;

	SpProtocol(Mailbox mb, String daemonAddress, String processName) throws GroupException {
		this.mb = mb;
		if (processName==null)
			processName=""+System.nanoTime()/1000%100000000;
		int ret=mb.C_connect(daemonAddress, processName, false, true);
		if (ret<0) throw new SpException(ret, null);
		boot();
	}
	
	@Override
	protected void cleanup() {
		super.cleanup();
		mb.C_disconnect();
		mb = null;
	}

	protected void createSessions(SpGroup group) {
		putSessions(group, new SpControlSession(mb), new SpDataSession(mb));
	}
	
	protected void read() throws GroupException {
		Mailbox.ReceiveArgs info=new Mailbox.ReceiveArgs();
		int allocate_size = 1024, ret=-1;
		ByteBuffer mess=null;
		try {
			do{
				mess = ByteBuffer.allocate(allocate_size);
				ret=mb.C_receive(info, mess);
				if (ret<0)
					allocate_size *= 2;
			} while(ret < 0);
		} catch(NullPointerException npe) {
			/* closing */
			return;
		}
		mess.flip();
		if ((info.service_type & SpService.REGULAR_MESS) == 0) {
			GroupConfiguration group = new SpGroup(info.sender);
			SpControlSession control = lookupControlSession(group);
			if (control==null)
				return;
			control.deliverView(info, mess);
		} else {
			// If this is a subcast, info.groups contains all targets
			// and then the enclosing group name
			for(String name: info.groups) {
				GroupConfiguration group = new SpGroup(name);
				SpDataSession data = lookupDataSession(group);
				// If we don't know the group, it is directed at the process itself
				if (data!=null) {
					data.deliverMessage(info,mess);
					return;
				}
			}
			// not delivered.
		}
	}
}
