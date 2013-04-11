
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

import java.util.concurrent.locks.Lock;

import net.sf.jgcs.ClosedSessionException;
import net.sf.jgcs.ControlSession;
import net.sf.jgcs.ExceptionListener;
import net.sf.jgcs.GroupConfiguration;
import net.sf.jgcs.GroupException;

/**
 * 
 * This class defines a AbstractControlSession. It partially implements the ControlSession interface
 * by handling the listeners. It provides methods for notification of listeners
 * that can be used by final implementations.
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public abstract class AbstractControlSession<
	P extends AbstractProtocol<P,DS,CS,G>,
	DS extends AbstractDataSession<P,DS,CS,G>,
	CS extends AbstractControlSession<P,DS,CS,G>,
	G extends GroupConfiguration>
			implements ControlSession {
	protected P protocol;
	protected DS dataSession;
	protected G group;
	protected Lock lock;

	private ExceptionListener exceptionListener;

	private boolean closed;
	
	/**
	 * Initializes any variables needed by this session.
	 *
	 */
	protected void boot() {
	}

	protected void cleanup() {
		try {
			exceptionListener = null;
			
			/* Try to leave. Will probably cause an exception. */
			leave();
		} catch (GroupException e) {
			/* Don't care, as the session is already closed. */
		} finally {
			closed = true;
		}
	}

	@Override
	public void close() throws GroupException {
		protocol.removeSessions(group);
	}
	
	public boolean isClosed() {
		try {
			lock.lock();
			return closed;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Sets the exception listener.
	 */
	public void setExceptionListener(ExceptionListener listener) {
		try {
			lock.lock();
			exceptionListener = listener;		
		} finally {
			lock.unlock();
		}
	}

	public ExceptionListener getExceptionListener() {
		try {
			lock.lock();
			return exceptionListener;
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * Verifies listener registratioVerifies listener registration.
	 * @return true if all listeners are registered
	 */
	protected boolean hasAllListeners() {
		return exceptionListener != null;
	}

	/**
	 * Notifies the exception listener.
	 * @param exception the exception to notify.
	 */
	protected void notifyExceptionListeners(GroupException exception) {
		/* Avoid NPE but invoke callback outside the lock.
		 * This means that there can be callbacks after close(),
		 * but avoids deadlocks.
		 */
		ExceptionListener listener = null;
		try {
			lock.lock();
			listener = exceptionListener;		
		} finally {
			lock.unlock();
		}
		if(listener != null)
			listener.onException(exception);
	}

	/**
	 * Check if the session has not been closed.
	 * @throws GroupException
	 */
	protected void onEntry() throws GroupException {
		if (isClosed()) throw new ClosedSessionException();
	}
}
