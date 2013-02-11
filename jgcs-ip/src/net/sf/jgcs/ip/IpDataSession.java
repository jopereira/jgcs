
/*
 * IP Multicast implementation of JGCS - Group Communication Service.
 * Copyright (C) 2006 Jose' Orlando Pereira, Universidade do Minho
 *
 * jop@di.uminho.pt - http://gsd.di.uminho.pt/~jop
 * jgcs@lasige.di.fc.ul.pt
 *
 * Departamento de Informatica, Universidade do Minho
 * Campus de Gualtar, 4710-057 Braga, Portugal
 *
 * See COPYING for licensing details.
 */
	  
package net.sf.jgcs.ip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;

import net.sf.jgcs.AbstractPollingDataSession;
import net.sf.jgcs.Annotation;
import net.sf.jgcs.Message;
import net.sf.jgcs.Service;
import net.sf.jgcs.UnsupportedServiceException;


public class IpDataSession extends AbstractPollingDataSession {
	private MulticastSocket sock;
	
	IpDataSession(MulticastSocket sock, IpProtocol protocol, IpGroup group) {
		super(protocol, group);
		this.sock=sock;
	}

	public Message createMessage() {
		return new IpMessage();
	}

	public void send(Message msg, Service service, Object cookie, SocketAddress destination, Annotation... annotation) throws IOException, UnsupportedServiceException {
		InetSocketAddress iaddr=(InetSocketAddress)destination;
		send(msg.getPayload(), ((IpService)service).getTtl(), iaddr.getAddress(), iaddr.getPort());
	}
	
	public void multicast(Message msg, Service service, Object cookie, Annotation... annotation) throws IOException, UnsupportedServiceException {
		send(msg.getPayload(), ((IpService)service).getTtl(), ((IpGroup)getGroup()).getGroupAddress(), ((IpGroup)getGroup()).getPort());
	}
	
	private synchronized void send(byte[] bs, int ttl, InetAddress dest, int port) throws IOException {
		DatagramPacket dgram=new DatagramPacket(bs, bs.length, dest, port);
		int old=sock.getTimeToLive();
		sock.setTimeToLive(ttl);
		sock.send(dgram);
		sock.setTimeToLive(old);
	}

	protected Message read() throws IOException {
		DatagramPacket dgram=new DatagramPacket(new byte[1024], 1024);
		sock.receive(dgram);
		return new IpMessage(dgram);		
	}

	protected void cleanup() {
		sock.close();
	}
}
