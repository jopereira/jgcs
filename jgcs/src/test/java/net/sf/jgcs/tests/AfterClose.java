/*
 * JGCS - Group Communication Service.
 * Copyright (C) 2013 José Orlando Pereira
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs.tests;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.sf.jgcs.ClosedProtocolException;
import net.sf.jgcs.ClosedSessionException;
import net.sf.jgcs.ControlSession;
import net.sf.jgcs.DataSession;
import net.sf.jgcs.GroupConfiguration;
import net.sf.jgcs.Message;
import net.sf.jgcs.Protocol;
import net.sf.jgcs.ProtocolFactory;
import net.sf.jgcs.Service;

import org.junit.BeforeClass;
import org.junit.Test;

public class AfterClose {
	private static ProtocolFactory factory;
	private static GroupConfiguration group;
	private static Service service;
	
	private static Protocol protocol;
	private static ControlSession cs;
	private static DataSession ds;
	
	private static Message msg;
	
	@BeforeClass
	public static void init() throws NamingException, IOException {
		Context ctx = new InitialContext();
		factory = (ProtocolFactory) ctx.lookup("myProto");
		group = (GroupConfiguration) ctx.lookup("myGroup");
		service = (Service) ctx.lookup("myService");

		protocol = factory.createProtocol();
		cs = protocol.openControlSession(group);
		ds = protocol.openDataSession(group);
		
		msg = ds.createMessage();
		msg.setPayload("hello world!".getBytes());
		
		protocol.close();
	}

	@Test(expected = ClosedProtocolException.class)
	public void openData() throws IOException {
		protocol.openDataSession(group);
	}

	@Test(expected = ClosedProtocolException.class)
	public void openControl() throws IOException {
		protocol.openControlSession(group);
	}

	@Test(expected = ClosedSessionException.class)
	public void join() throws IOException {
		cs.join();
	}

	@Test(expected = ClosedSessionException.class)
	public void leave() throws IOException {
		cs.leave();
	}

	@Test(expected = ClosedSessionException.class)
	public void createMessage() throws IOException {
		ds.createMessage();
	}

	@Test(expected = ClosedSessionException.class)
	public void multicast() throws IOException {
		ds.multicast(msg, service, null);
	}
}
