
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
package net.sf.jgcs;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * This class defines a AbstractProtocol
 * It partially implements a protocol with the mappings between a configuration
 * and a pair of control and data sessions.
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public abstract class AbstractProtocol implements Protocol {
	protected Map<GroupConfiguration,AbstractControlSession> controlSessions;
	protected Map<GroupConfiguration,AbstractDataSession> dataSessions;

	protected void boot() {
		if (controlSessions!=null)
			return;
		controlSessions=new HashMap<GroupConfiguration,AbstractControlSession>();
		dataSessions=new HashMap<GroupConfiguration,AbstractDataSession>();
	}
	
	protected synchronized void putSessions(GroupConfiguration g, AbstractControlSession control, AbstractDataSession data) {
		controlSessions.put(g, control);
		dataSessions.put(g, data);
	}

	protected synchronized void removeSessions(GroupConfiguration g) {
		controlSessions.remove(g);
		dataSessions.remove(g);
	}

	protected synchronized AbstractControlSession lookupControlSession(GroupConfiguration g) {
		boot();
		return controlSessions.get(g);
	}

	protected synchronized AbstractDataSession lookupDataSession(GroupConfiguration g) {
		boot();
		return dataSessions.get(g);
	}
}
