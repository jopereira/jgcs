/*
 * JGCS - Group Communication Service.
 * Copyright (C) 2006 Nuno Carvalho, Universidade de Lisboa
 * Copyright (C) 2013 José Orlando Pereira
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs.test;

import net.sf.jgcs.ProtocolFactory;
import net.sf.jgcs.Service;
import net.sf.jgcs.jgroups.JGroupsGroup;
import net.sf.jgcs.jgroups.JGroupsProtocolFactory;
import net.sf.jgcs.jgroups.JGroupsService;

/**
 * Hard-coded test application.
 */
public class HardcodedTest {
	
	public static void main(String[] args) {
		try {

			ProtocolFactory pf = new JGroupsProtocolFactory();
			
			JGroupsGroup g = new JGroupsGroup("MyGroup");

			Service s = new JGroupsService();
			
			Runnable test = new Application(pf, g, s);
			test.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
