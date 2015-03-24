/*
 * JGCS - Group Communication Service.
 * Copyright (C) 2013 José Orlando Pereira
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import mockit.Mocked;
import net.sf.jgcs.ControlSession;
import net.sf.jgcs.DataSession;
import net.sf.jgcs.GroupConfiguration;
import net.sf.jgcs.Protocol;
import net.sf.jgcs.ProtocolFactory;
import net.sf.jgcs.Service;

import org.junit.BeforeClass;
import org.junit.Test;

public class OpenClose {
	private static ProtocolFactory factory;
	private static GroupConfiguration group;
	private static Service service;
	
	@BeforeClass
	public static void init() throws NamingException {
		Context ctx = new InitialContext();
		factory = (ProtocolFactory) ctx.lookup("myProto");
		group = (GroupConfiguration) ctx.lookup("myGroup");
		service = (Service) ctx.lookup("myService");
	}

	public void createProtocol() throws IOException {
		Protocol protocol = factory.createProtocol();
		
		assertNotNull(protocol);
		assertFalse(protocol.isClosed());
		
		protocol.close();

		assertTrue(protocol.isClosed());
	}

	@Test(expected = ClassCastException.class)
	public void invalidOpenControl(@Mocked GroupConfiguration invalid) throws IOException {
		Protocol protocol = factory.createProtocol();

		try {
			protocol.openControlSession(invalid);
		} finally {
			protocol.close();
		}
	}

	@Test(expected = ClassCastException.class)
	public void invalidOpenData(@Mocked GroupConfiguration invalid) throws IOException {
		Protocol protocol = factory.createProtocol();

		try {
			protocol.openDataSession(invalid);
		} finally {
			protocol.close();
		}
	}

	@Test
	public void openOnce() throws IOException {
		Protocol protocol = factory.createProtocol();

		ControlSession cs1 = protocol.openControlSession(group);
		assertNotNull(cs1);

		DataSession ds1 = protocol.openDataSession(group);
		assertNotNull(ds1);

		protocol.close();
	}
	
	@Test
	public void openAgain() throws IOException {
		Protocol protocol = factory.createProtocol();

		ControlSession cs1 = protocol.openControlSession(group);
		DataSession ds1 = protocol.openDataSession(group);
		
		ControlSession cs2 = protocol.openControlSession(group);
		DataSession ds2 = protocol.openDataSession(group);
		
		assertSame(cs1, cs2);
		assertSame(ds1, ds2);

		protocol.close();
	}

	@Test
	public void closeByControl() throws IOException {
		Protocol protocol = factory.createProtocol();

		ControlSession cs = protocol.openControlSession(group);
		assertFalse(cs.isClosed());

		DataSession ds = protocol.openDataSession(group);
		assertFalse(ds.isClosed());
		
		cs.close();
		assertTrue(cs.isClosed());
		assertTrue(ds.isClosed());
		
		protocol.close();
	}

	@Test
	public void closeByData() throws IOException {
		Protocol protocol = factory.createProtocol();

		ControlSession cs = protocol.openControlSession(group);
		assertFalse(cs.isClosed());

		DataSession ds = protocol.openDataSession(group);
		assertFalse(ds.isClosed());
		
		ds.close();
		assertTrue(cs.isClosed());
		assertTrue(ds.isClosed());
		
		protocol.close();
	}

	@Test
	public void closeByProtocol() throws IOException {
		Protocol protocol = factory.createProtocol();

		ControlSession cs = protocol.openControlSession(group);
		assertFalse(cs.isClosed());

		DataSession ds = protocol.openDataSession(group);
		assertFalse(ds.isClosed());
		
		protocol.close();
		assertTrue(cs.isClosed());
		assertTrue(ds.isClosed());
	}
}
