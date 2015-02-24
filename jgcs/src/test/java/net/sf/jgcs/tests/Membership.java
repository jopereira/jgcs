/*
 * JGCS - Group Communication Service.
 * Copyright (C) 2013 José Orlando Pereira
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import mockit.Expectations;
import mockit.Mocked;
import net.sf.jgcs.DataSession;
import net.sf.jgcs.GroupConfiguration;
import net.sf.jgcs.GroupException;
import net.sf.jgcs.InvalidStateException;
import net.sf.jgcs.MembershipListener;
import net.sf.jgcs.MembershipSession;
import net.sf.jgcs.Protocol;
import net.sf.jgcs.ProtocolFactory;
import net.sf.jgcs.Service;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class Membership {
	private static ProtocolFactory factory;
	private static GroupConfiguration group;
	private static Service service;
	
	private static Protocol protocol;
	private static MembershipSession cs;
	private static DataSession ds;
	
	@BeforeClass
	public static void init() throws NamingException, GroupException {
		Context ctx = new InitialContext();
		factory = (ProtocolFactory) ctx.lookup("myProto");
		group = (GroupConfiguration) ctx.lookup("myGroup");
		service = (Service) ctx.lookup("myService");

		protocol = factory.createProtocol();
		cs = (MembershipSession) protocol.openControlSession(group);
		ds = protocol.openDataSession(group);
	}

	@Test
	public void joinAndLeave(@Mocked final MembershipListener ml) throws IOException, InterruptedException {
		final BlockingQueue<net.sf.jgcs.Membership> queue = new LinkedBlockingQueue<net.sf.jgcs.Membership>();
		
		new Expectations() {
			{
				ml.onMembershipChange();
				ml.onExcluded(); minTimes = 0;
			}
		};

		cs.setMembershipListener(new MembershipListener() {
			public void onMembershipChange() {
				try {
					ml.onMembershipChange();
					queue.add(cs.getMembership());
					
				} catch (InvalidStateException e) {
					Assert.fail(e.getMessage());
				}
			}
			
			@Override
			public void onExcluded() {
				ml.onMembershipChange();
			}
		});
		
		cs.join();
		
		net.sf.jgcs.Membership m = queue.take();
		
		assertTrue(m.getJoinedMembers().size()==1);
		assertEquals(m.getJoinedMembers().get(0), cs.getLocalAddress());
		
		assertEquals(m.getLocalRank(), m.getCoordinatorRank());
		assertEquals(m.getMemberAddress(m.getLocalRank()), cs.getLocalAddress());

		assertTrue(m.getLeavedMembers().isEmpty());
		assertTrue(m.getFailedMembers().isEmpty());
		
		cs.leave();
	}
	
	@AfterClass
	public static void cleanup() throws IOException {
		protocol.close();
	}
}
