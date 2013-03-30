
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
package net.sf.jgcs.spi;

import java.util.HashMap;
import java.util.Map;

import net.sf.jgcs.ControlSession;
import net.sf.jgcs.DataSession;
import net.sf.jgcs.GroupConfiguration;
import net.sf.jgcs.JGCSException;
import net.sf.jgcs.Protocol;

/**
 * 
 * This class defines a AbstractProtocol
 * It partially implements a protocol with the mappings between a configuration
 * and a pair of control and data sessions.
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public abstract class AbstractProtocol<
		P extends AbstractProtocol<P,DS,CS,G>,
		DS extends AbstractDataSession<P,DS,CS,G>,
		CS extends AbstractControlSession<P,DS,CS,G>,
		G extends GroupConfiguration>
			implements Protocol {
	protected Map<G,CS> controlSessions;
	protected Map<G,DS> dataSessions;

	protected void boot() {
		if (controlSessions!=null)
			return;
		controlSessions=new HashMap<G,CS>();
		dataSessions=new HashMap<G,DS>();
	}
	
	protected abstract void createSessions(G group) throws JGCSException;

	@SuppressWarnings("unchecked")
	protected synchronized void putSessions(G g, CS control, DS data) {
		control.protocol = (P)this;
		control.dataSession = data;
		control.group = g;
		
		data.protocol = (P)this;
		data.controlSession = control;
		data.group = g;
		
		controlSessions.put(g, control);
		dataSessions.put(g, data);
	}

	protected synchronized void removeSessions(G g) {
		controlSessions.remove(g);
		dataSessions.remove(g);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DataSession openDataSession(GroupConfiguration group) throws JGCSException {
		DataSession data=lookupDataSession(group);
		if (data==null) {
			createSessions((G)group);
			data=lookupDataSession(group);
		}
		return data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ControlSession openControlSession(GroupConfiguration group) throws JGCSException {
		ControlSession control=lookupControlSession(group);
		if (control==null) {
			createSessions((G)group);
			control=lookupControlSession(group);
		}
		return control;
	}

	protected synchronized CS lookupControlSession(GroupConfiguration g) {
		boot();
		return controlSessions.get(g);
	}

	protected synchronized DS lookupDataSession(GroupConfiguration g) {
		boot();
		return dataSessions.get(g);
	}
}
