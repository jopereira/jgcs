/*
 * JGCS - Group Communication Service.
 * Copyright (C) 2013 José Orlando Pereira
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs.tests;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.sf.jgcs.ControlSession;
import net.sf.jgcs.GroupConfiguration;
import net.sf.jgcs.InvalidStateException;
import net.sf.jgcs.Protocol;
import net.sf.jgcs.ProtocolFactory;
import net.sf.jgcs.Service;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
public class JoinLeave {
	private ProtocolFactory factory;
	private GroupConfiguration group;
	private Service service;
	
	@BeforeClass
	public void init() throws NamingException {
		Context ctx = new InitialContext();
		factory = (ProtocolFactory) ctx.lookup("myProto");
		group = (GroupConfiguration) ctx.lookup("myGroup");
		service = (Service) ctx.lookup("myService");
	}

	public void joinLeave() throws IOException, InterruptedException {
		Protocol protocol = factory.createProtocol();

		ControlSession cs = protocol.openControlSession(group);
		
		cs.join();
		
		Thread.sleep(1000);
		
		assertNotNull(cs.getLocalAddress());

		cs.leave();
		
		cs.close();
		
		protocol.close();
	}

	public void joinClose() throws IOException, InterruptedException {
		Protocol protocol = factory.createProtocol();

		ControlSession cs = protocol.openControlSession(group);
		
		cs.join();
		
		Thread.sleep(1000);

		assertNotNull(cs.getLocalAddress());

		cs.close();
		
		assertTrue(cs.isClosed());
		assertNull(cs.getLocalAddress());
		
		protocol.close();
	}

	@Test(expectedExceptions = { InvalidStateException.class } )
	public void joinAgain() throws IOException, InterruptedException {
		Protocol protocol = factory.createProtocol();

		ControlSession cs = protocol.openControlSession(group);
		
		cs.join();		

		Thread.sleep(1000);
		
		try {
			cs.join();
		} finally {
			protocol.close();
		}
	}

	@Test(expectedExceptions = { InvalidStateException.class } )
	public void leaveAgain() throws IOException, InterruptedException {
		Protocol protocol = factory.createProtocol();

		ControlSession cs = protocol.openControlSession(group);
		
		cs.join();		

		Thread.sleep(1000);
		
		cs.leave();		

		Thread.sleep(1000);
		
		try {
			cs.leave();
		} finally {
			protocol.close();
		}
	}
}
