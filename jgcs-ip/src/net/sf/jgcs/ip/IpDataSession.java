/*
 * IP Multicast implementation of JGCS - Group Communication Service.
 * Copyright (C) 2006,2013 José Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs.ip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

import net.sf.jgcs.Annotation;
import net.sf.jgcs.GroupException;
import net.sf.jgcs.Message;
import net.sf.jgcs.Service;
import net.sf.jgcs.annotation.PointToPoint;
import net.sf.jgcs.spi.AbstractPollingDataSession;

class IpDataSession extends AbstractPollingDataSession<IpProtocol,IpDataSession,IpControlSession,IpGroup> {
	private MulticastSocket sock;
	
	IpDataSession(MulticastSocket sock) {
		this.sock=sock;
		boot();
	}

	@Override
	public Message createMessage() throws GroupException {
		try {
			lock.lock();
			onEntry();
			return new IpMessage();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void multicast(Message msg, Service service, Object cookie, Annotation... annotation) throws IOException {
		InetSocketAddress iaddr=getGroup().getAddress();
			
		for(Annotation a: annotation)
			if (a instanceof PointToPoint)
				iaddr = (InetSocketAddress)((PointToPoint)a).getDestination();
		
		IpMessage m = (IpMessage) msg;
			
		try {
			send(m.getPayload(), ((IpService)service).getTtl(), iaddr);
		} catch(SocketException se) {
			onEntry();
			throw se;
		}
	}
	
	private void send(byte[] bs, int ttl, InetSocketAddress dest) throws IOException {
		DatagramPacket dgram=new DatagramPacket(bs, bs.length, dest);
		int old=sock.getTimeToLive();
		sock.setTimeToLive(ttl);
		sock.send(dgram);
		sock.setTimeToLive(old);
	}

	@Override
	protected void read() throws IOException {
		DatagramPacket dgram=new DatagramPacket(new byte[1024], 1024);
		sock.receive(dgram);
		notifyListeners(new IpMessage(dgram), new IpService("0"));		
	}

	@Override
	protected void cleanup() {
		super.cleanup();
		sock.close();
	}
}
