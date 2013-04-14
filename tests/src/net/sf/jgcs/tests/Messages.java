/*
 * JGCS - Group Communication Service.
 * Copyright (C) 2013 José Orlando Pereira
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs.tests;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import net.sf.jgcs.ControlSession;
import net.sf.jgcs.DataSession;
import net.sf.jgcs.GroupConfiguration;
import net.sf.jgcs.GroupException;
import net.sf.jgcs.Message;
import net.sf.jgcs.MessageListener;
import net.sf.jgcs.Protocol;
import net.sf.jgcs.ProtocolFactory;
import net.sf.jgcs.Service;
import net.sf.jgcs.ServiceListener;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
public class Messages {
	private ProtocolFactory factory;
	private GroupConfiguration group;
	private Service service;
	
	private Protocol protocol;
	private ControlSession cs;
	private DataSession ds;
	
	@BeforeClass
	public void init() throws NamingException, GroupException {
		Context ctx = new InitialContext();
		factory = (ProtocolFactory) ctx.lookup("myProto");
		group = (GroupConfiguration) ctx.lookup("myGroup");
		service = (Service) ctx.lookup("myService");

		protocol = factory.createProtocol();
		cs = protocol.openControlSession(group);
		ds = protocol.openDataSession(group);

		cs.join();
	}

	@Test(expectedExceptions = { ClassCastException.class })
	public void invalidMessage(@Mocked final Message msg) throws IOException, InterruptedException {
		ds.multicast(msg, service, null);		
	}

	@Test(expectedExceptions = { ClassCastException.class })
	public void invalidService(@Mocked final Service s) throws IOException, InterruptedException {
		Message msg = ds.createMessage();

		ds.multicast(msg, s, null);		
	}

	public void message(@Mocked final MessageListener ml) throws IOException, InterruptedException {

		new NonStrictExpectations() {
			{
				ml.onMessage(null); returns(null);
			}
		};

		ds.setMessageListener(ml);
		
		Message m1 = ds.createMessage();
		final byte[] payload = "hello world!".getBytes();
		m1.setPayload(payload);
		
		ds.multicast(m1, service, null);
		
		Thread.sleep(1000);
		
		new Verifications() {{
			Message m2;
			ml.onMessage(m2 = withCapture());
			
			assertNotNull(m2);
			assertNotNull(m2.getPayload());
			assertEquals(payload, m2.getPayload());
		}};
	}

	public void service(@Mocked final MessageListener ml,
						@Mocked final ServiceListener sl) throws IOException, InterruptedException {

		final Object cookie = "cookie";
		
		new NonStrictExpectations() {
			{
				ml.onMessage((Message)any); returns(cookie);
				
				sl.onServiceEnsured(cookie, (Service)any); minTimes = 1;
			}
		};
		
		ds.setMessageListener(ml);
		ds.setServiceListener(sl);
		
		Message m1 = ds.createMessage();
		final byte[] payload = "hello world!".getBytes();
		m1.setPayload(payload);
		
		ds.multicast(m1, service, null);

		Thread.sleep(1000);
		
		new Verifications() {{
			List<Service> s = new ArrayList<Service>();
			sl.onServiceEnsured(cookie, withCapture(s));
			assertTrue(s.size()>=1);
			assertTrue(s.get(s.size()-1).satisfies(service));
		}};
	}

	@AfterClass
	public void cleanup() throws IOException {
		protocol.close();
	}
}
