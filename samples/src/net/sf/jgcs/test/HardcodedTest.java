
/*
 * JGCS - Group Communication Service.
 * Copyright (C) 2006 Nuno Carvalho, Universidade de Lisboa
 *
 * jgcs@lasige.di.fc.ul.pt
 *
 * Departamento de Informatica, Universidade de Lisboa
 * Bloco C6, Faculdade de Ciências, Campo Grande, 1749-016 Lisboa, Portugal.
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
			
			JGroupsGroup g = new JGroupsGroup("MyGroup", "sequencer.xml");

			Service s = new JGroupsService("vsc+total");
			
			Runnable test = new Application(pf, g, s);
			test.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
