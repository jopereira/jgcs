
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
import net.sf.jgcs.JGCSException;
import net.sf.jgcs.spi.AbstractPollingProtocol;
import net.sf.jgcs.spread.jni.Mailbox;

public class SpProtocol extends AbstractPollingProtocol<SpProtocol,SpDataSession,SpControlSession,SpGroup> {
	private Mailbox mb;
	private SpDataSession defaultData;

	SpProtocol(Mailbox mb, String daemonAddress, String processName) throws JGCSException {
		this.mb = mb;
		if (processName==null)
			processName=""+System.nanoTime()/1000%100000000;
		int ret=mb.C_connect(daemonAddress, processName, false, true);
		if (ret<0) throw new SpException(ret, null);
	}

	protected synchronized void createSessions(SpGroup group) {
		putSessions(group, new SpControlSession(mb), new SpDataSession(mb));
	}
	
	protected synchronized void removeSessions(SpGroup group) {
		SpControlSession control=(SpControlSession)lookupControlSession(group);
		control.handleClose();
		super.removeSessions(group);
	}

	protected void read() {
		Mailbox.ReceiveArgs info=new Mailbox.ReceiveArgs();
		int allocate_size = 1024, ret=-1;
		ByteBuffer mess=null;
		do{
			mess = ByteBuffer.allocate(allocate_size);
			ret=mb.C_receive(info, mess);
			if (ret<0)
				allocate_size *= 2;
		}while(ret < 0);
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
			String name = info.groups[info.groups.length-1];
			GroupConfiguration group = new SpGroup(name);
			SpDataSession data = lookupDataSession(group);
			// If we don't know the group, it is directed at the process itself
			if (data==null)
				data=defaultData;
			data.deliverMessage(info,mess);
		}
	}
}
