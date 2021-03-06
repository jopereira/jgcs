/*
 * JGCS - Group Communication Service.
 * Copyright (C) 2006 Nuno Carvalho, Universidade de Lisboa
 * Copyright (C) 2013 Jose' Orlando Pereira
 *
 * http://github.com/jopereira/jgcs
 *
 * See COPYING for licensing details.
 */

package net.sf.jgcs.spi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.sf.jgcs.ClosedProtocolException;
import net.sf.jgcs.ControlSession;
import net.sf.jgcs.DataSession;
import net.sf.jgcs.GroupConfiguration;
import net.sf.jgcs.GroupException;
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
	
	private boolean closed;

	protected AbstractProtocol() {
		controlSessions=new HashMap<G,CS>();
		dataSessions=new HashMap<G,DS>();		
	}
	
	/**
	 * Called when a session is required for a previously unused group configuration.
	 * 
	 * @param group group configuration
	 * @throws GroupException if sessions cannot be created
	 * @throws ClassCastException if the group cannot be cast to the right class
	 */
	protected abstract void createSessions(G group) throws GroupException, ClassCastException;

	/**
	 * This should be called only by derived classes within the createSession() method
	 * to register new sessions for the given group.
	 * 
	 * @param g group configuration
	 * @param control control session
	 * @param data data session
	 */
	@SuppressWarnings("unchecked")
	protected void putSessions(G g, CS control, DS data) {
		Lock lock = new ReentrantLock();
		
		control.protocol = (P)this;
		control.dataSession = data;
		control.group = g;
		control.lock = lock;
		
		data.protocol = (P)this;
		data.controlSession = control;
		data.group = g;
		data.lock = lock;
		
		controlSessions.put(g, control);
		dataSessions.put(g, data);
		
		data.boot();
	}

	protected void removeSessions(G g) {
		CS control;
		DS data;
		synchronized (this) {
			control = controlSessions.remove(g);
			data = dataSessions.remove(g);			
		}
		/* Might happen if being closed concurrently by two threads. */
		if (control!=null) {
			try {
				control.lock.lock();
				control.cleanup();
				data.cleanup();
			} finally {
				control.lock.unlock();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public synchronized DataSession openDataSession(GroupConfiguration group) throws GroupException {
		onEntry();
		DataSession data=dataSessions.get(group);
		if (data==null) {
			createSessions((G)group);
			data=dataSessions.get(group);
		}
		return data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized ControlSession openControlSession(GroupConfiguration group) throws GroupException {
		onEntry();
		ControlSession control=controlSessions.get(group);
		if (control==null) {
			createSessions((G)group);
			control=controlSessions.get(group);
		}
		return control;
	}
	
	protected synchronized CS lookupControlSession(GroupConfiguration g) throws GroupException {
		onEntry();
		return controlSessions.get(g);
	}

	protected synchronized DS lookupDataSession(GroupConfiguration g) throws GroupException {
		onEntry();
		return dataSessions.get(g);
	}
	
	@Override
	public synchronized void close() throws IOException {
		if (isClosed())
			return;
		cleanup();
	}
	
	protected void cleanup() {
		Collection<CS> css;
		css = controlSessions.values();
		controlSessions = null;
		dataSessions = null;
		closed = true;
		for(CS c: css) {
			try {
				c.lock.lock();
				c.cleanup();
				c.dataSession.cleanup();
			} finally {
				c.lock.unlock();
			}
		}		
	}
	
	@Override
	public boolean isClosed() {
		return closed;
	}

	protected void notifyExceptionListeners(GroupException exception) { 
		Collection<CS> css;
		synchronized(this) {
			css = new ArrayList<CS>(); 
			css.addAll(controlSessions.values());
		}
		for(CS c: css)
			c.notifyExceptionListeners(exception);
	}
	
	/**
	 * Check if the protocol has not been closed.
	 * @throws GroupException
	 */
	protected void onEntry() throws GroupException {
		if (isClosed()) throw new ClosedProtocolException();
	}
}